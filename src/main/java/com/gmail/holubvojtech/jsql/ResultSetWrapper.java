package com.gmail.holubvojtech.jsql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public abstract class ResultSetWrapper implements AutoCloseable {
   protected ResultSet rs;

   public ResultSetWrapper(ResultSet var1) {
      this.rs = var1;
   }

   public String getString(int var1) {
      try {
         return this.rs.getString(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public boolean getBoolean(int var1) {
      try {
         return this.rs.getBoolean(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public byte getByte(int var1) {
      try {
         return this.rs.getByte(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public short getShort(int var1) {
      try {
         return this.rs.getShort(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public int getInt(int var1) {
      try {
         return this.rs.getInt(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public long getLong(int var1) {
      try {
         return this.rs.getLong(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public float getFloat(int var1) {
      try {
         return this.rs.getFloat(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public double getDouble(int var1) {
      try {
         return this.rs.getDouble(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public byte[] getBytes(int var1) {
      try {
         return this.rs.getBytes(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public Date getDate(int var1) {
      try {
         return this.rs.getDate(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public Time getTime(int var1) {
      try {
         return this.rs.getTime(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public Timestamp getTimestamp(int var1) {
      try {
         return this.rs.getTimestamp(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public BigDecimal getBigDecimal(int var1) {
      try {
         return this.rs.getBigDecimal(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public String getString(String var1) {
      return this.getString(this.findColumn(var1));
   }

   public boolean getBoolean(String var1) {
      return this.getBoolean(this.findColumn(var1));
   }

   public byte getByte(String var1) {
      return this.getByte(this.findColumn(var1));
   }

   public short getShort(String var1) {
      return this.getShort(this.findColumn(var1));
   }

   public int getInt(String var1) {
      return this.getInt(this.findColumn(var1));
   }

   public long getLong(String var1) {
      return this.getLong(this.findColumn(var1));
   }

   public float getFloat(String var1) {
      return this.getFloat(this.findColumn(var1));
   }

   public double getDouble(String var1) {
      return this.getDouble(this.findColumn(var1));
   }

   public byte[] getBytes(String var1) {
      return this.getBytes(this.findColumn(var1));
   }

   public Date getDate(String var1) {
      return this.getDate(this.findColumn(var1));
   }

   public Time getTime(String var1) {
      return this.getTime(this.findColumn(var1));
   }

   public Timestamp getTimestamp(String var1) {
      return this.getTimestamp(this.findColumn(var1));
   }

   public BigDecimal getBigDecimal(String var1) {
      return this.getBigDecimal(this.findColumn(var1));
   }

   public int findColumn(String var1) {
      try {
         return this.rs.findColumn(var1);
      } catch (SQLException var3) {
         throw new RuntimeException(var3);
      }
   }

   public boolean isClosed() {
      try {
         return this.rs.isClosed();
      } catch (SQLException var2) {
         throw new RuntimeException(var2);
      }
   }

   public void close() {
      try {
         this.rs.close();
      } catch (SQLException var2) {
         throw new RuntimeException(var2);
      }
   }
}
