package com.gmail.holubvojtech.jsql;

public class ColumnOrder {
   private String column;
   private Order order;

   public ColumnOrder(String var1, Order var2) {
      this.column = var1;
      this.order = var2;
   }

   public String getColumn() {
      return this.column;
   }

   public Order getOrder() {
      return this.order;
   }

   public String toString() {
      return Utils.grave(this.column) + " " + this.order.name();
   }
}
