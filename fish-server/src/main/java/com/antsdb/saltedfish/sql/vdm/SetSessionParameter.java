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

public class SetSessionParameter extends Statement {
    String name;
    Operator op;
    
    
    public SetSessionParameter(String name, Operator op) {
        super();
        this.name = name;
        this.op = op;
    }

    @Override
    public Object run(VdmContext ctx, Parameters params) {
        Object val = Util.eval(ctx, this.op, params, 0);
        ctx.getSession().getConfig().set(this.name, val == null ? null : val.toString());
        return null;
    }

}
