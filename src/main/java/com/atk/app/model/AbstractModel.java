package com.atk.app.model;

import java.io.Serializable;

/**
 * Kelas abstrak yang menyediakan fungsionalitas dasar untuk semua model
 */
public abstract class AbstractModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Metode untuk validasi data model
     * @return true jika data valid, false jika tidak
     */
    public abstract boolean validate();
    
    /**
     * Metode untuk mendapatkan nama tabel yang terkait dengan model ini
     * @return nama tabel dalam database
     */
    public abstract String getTableName();
    
    /**
     * Metode untuk mendapatkan primary key dari model
     * @return nilai primary key
     */
    public abstract Object getPrimaryKey();
    
    /**
     * Metode untuk mengkonversi model ke string untuk keperluan debugging
     * @return representasi string dari model
     */
    @Override
    public abstract String toString();
} 