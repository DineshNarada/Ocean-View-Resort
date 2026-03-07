package com.orrs.dao;

import com.orrs.domain.RoomType;

/**
 * RoomType Data Access Object Interface
 */
public interface IRoomTypeDAO extends IGenericDAO<RoomType> {
    
    /**
     * Find room type by name
     */
    RoomType findByTypeName(String typeName) throws Exception;
}
