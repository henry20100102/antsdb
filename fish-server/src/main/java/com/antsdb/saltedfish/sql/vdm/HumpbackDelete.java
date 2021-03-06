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
package com.antsdb.saltedfish.sql.vdm;

import com.antsdb.saltedfish.nosql.GTable;
import com.antsdb.saltedfish.nosql.HumpbackError;
import com.antsdb.saltedfish.sql.OrcaException;

/**
 * 
 * @author wgu0
 */
public class HumpbackDelete extends Instruction {
	int tableId;
	byte[] key;
	
	public HumpbackDelete(int tableId, byte[] key) {
		this.tableId = tableId;
		this.key = key;
	}

	@Override
	public Object run(VdmContext ctx, Parameters params, long pMaster) {
		GTable table = ctx.getHumpback().getTable(this.tableId);
		if (table == null) {
			throw new OrcaException("table {} is not found", tableId);
		}
		long trxid = ctx.getHumpback().getTrxMan().getNewVersion();
		HumpbackError error = table.delete(trxid, key, ctx.getSession().getConfig().getLockTimeout());
		if (error != HumpbackError.SUCCESS) {
			throw new OrcaException(error);
		}
		return 1;
	}

}
