/*-------------------------------------------------------------------------------------------------
 _______ __   _ _______ _______ ______  ______
 |_____| | \  |    |    |______ |     \ |_____]
 |     | |  \_|    |    ______| |_____/ |_____]

 Copyright (c) 2016, antsdb.com and/or its affiliates. All rights reserved. *-xguo0<@

 This program is free software: you can redistribute it and/or modify it under the terms of the
 GNU Affero General Public License, version 3, as published by the Free Software Foundation.

 You should have received a copy of the GNU Affero General Public License along with this program.
 If not, see <https://www.gnu.org/licenses/agpl-3.0.txt>
-------------------------------------------------------------------------------------------------*/
package com.antsdb.saltedfish.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;

import com.antsdb.saltedfish.cpp.MemoryManager;
import com.antsdb.saltedfish.server.mysql.ChannelWriterNio;
import com.antsdb.saltedfish.server.mysql.MysqlSession;
import com.antsdb.saltedfish.sql.vdm.ShutdownException;
import com.antsdb.saltedfish.util.UberUtil;

/**
 * 
 * @author *-xguo0<@
 */
public class SimpleSocketWorker implements Runnable {
    static Logger _log = UberUtil.getThisLogger();
    private SaltedFish fish;
    private SocketChannel channel;
    private MysqlSession mysession;
    private ChannelWriterNio out;
    private ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 16);
    
    public SimpleSocketWorker(SaltedFish fish, SocketChannel channel) {
        this.buf.order(ByteOrder.LITTLE_ENDIAN);
        this.fish = fish;
        this.channel = channel;
        this.out = new ChannelWriterNio(channel);
        SocketAddress remote = null;
        try {
            remote = channel.getRemoteAddress();
        }
        catch (IOException x) {
            _log.warn("something is wrong", x);
        }
        this.mysession = new MysqlSession(fish, out, remote);
    }

    @Override
    public void run() {
        try {
            run0();
        }
        catch (ShutdownException x) {
            shutdown();
        }
        catch (EOFException x) {
        }
        catch (Exception x) {
            _log.error("error", x);
        }
        finally {
            this.mysession.close();
            MemoryManager.threadEnd();
        }
    }

    private void shutdown() {
        this.fish.close();
        System.exit(0);
    }

    private void run0() throws IOException {
        this.mysession.onConnect();
        for (;;) {
            this.buf.clear();
            readHeader();
            readBody();
            try {
                this.buf.flip();
                this.mysession.run(this.buf);
            }
            finally {
                this.buf.clear();
            }
        }
    }

    private void readBody() throws IOException {
        int size = this.buf.getInt(0) & 0xffffff;
        if (size == 0) {
            throw new IllegalArgumentException();
        }
        int packetSize = size + 4;
        if (buf.capacity() < packetSize) {
            // grow the buffer size
            grow(packetSize);
        }
        buf.limit(packetSize);
        for(;;) {
            int nread = this.channel.read(this.buf);
            if (nread < 0) {
                // connection closed
                this.mysession.close();
                throw new EOFException();
            }
            if (this.buf.position() < size + 4) {
                // not enough bytes for the body
                continue;
            }
            break;
        }
    }

    private void readHeader() throws IOException {
        this.buf.limit(4);
        for(;;) {
            int nread = this.channel.read(this.buf);
            if (nread < 0) {
                // connection closed
                this.mysession.close();
                throw new EOFException();
            }
            if (this.buf.position() < 4) {
                // not enough bytes for the header
                continue;
            }
            break;
        }
    }

    private void grow(int packetSize) {
        ByteBuffer newbuf = ByteBuffer.allocateDirect(packetSize);
        newbuf.order(ByteOrder.LITTLE_ENDIAN);
        this.buf.flip();
        newbuf.put(this.buf);
        this.buf = newbuf;
    }

}
