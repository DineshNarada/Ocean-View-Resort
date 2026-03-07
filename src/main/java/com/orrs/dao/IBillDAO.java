package com.orrs.dao;

import com.orrs.domain.Bill;
import java.util.List;

/**
 * Bill Data Access Object Interface
 */
public interface IBillDAO extends IGenericDAO<Bill> {
    
    /**
     * Find bill by reservation ID
     */
    Bill findByReservationId(int reservationId) throws Exception;
    
    /**
     * Get all unpaid bills
     */
    List<Bill> findUnpaidBills() throws Exception;
}
