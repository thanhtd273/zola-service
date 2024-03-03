package com.thanhtd.zola.service;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.LogicException;
import com.thanhtd.zola.dto.RoleInfo;
import com.thanhtd.zola.model.Role;

public interface RoleService {
    Role findByRoleId(Long roleId) throws LogicException;

    Role createRole(RoleInfo roleInfo) throws LogicException;

    Role updateRole(RoleInfo roleInfo) throws LogicException;

    ErrorCode deleteRole(Long roleId) throws LogicException;
}
