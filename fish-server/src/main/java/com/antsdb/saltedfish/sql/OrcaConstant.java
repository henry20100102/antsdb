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
package com.antsdb.saltedfish.sql;

import com.antsdb.saltedfish.sql.meta.TableId;

public class OrcaConstant {
    public static final String SYSNS = "__SYS";
    
    public static final String TABLENAME_SYSSEQUENCE = "SYSSEQUENCE";
    public static final String TABLENAME_SYSTABLE = "SYSTABLE";
    public static final String TABLENAME_SYSCOLUMN = "SYSCOLUMN";
    public static final String TABLENAME_SYSPARAM = "SYSPARAM";
    public static final String TABLENAME_SYSRULE = "SYSRULE";
    public static final String TABLENAME_SYSRULECOL = "SYSRULECOL";
    
    public static final int TABLEID_SYSSEQUENCE = -TableId.SYSSEQUENCE.ordinal();
    public static final int TABLEID_SYSTABLE = -TableId.SYSTABLE.ordinal();
    public static final int TABLEID_SYSCOLUMN = -TableId.SYSCOLUMN.ordinal();
    public static final int TABLEID_SYSPARAM = -TableId.SYSPARAM.ordinal();
    public static final int TABLEID_SYSRULE = -TableId.SYSRULE.ordinal();
    public static final int TABLEID_SYSRULECOL = -TableId.SYSRULECOL.ordinal();
}