package com.gmail.holubvojtech.ultimatefriends;

import java.util.List;

public class ListPaginator<T> {
   private List<T> list;
   private int perPage = 20;
   private int page = 0;

   public ListPaginator(List<T> var1) {
      this.list = var1;
   }

   public ListPaginator perPage(int var1) {
      if (var1 <= 0) {
         var1 = 1;
      }

      this.perPage = var1;
      return this;
   }

   public ListPaginator page(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      this.page = var1;
      return this;
   }

   public int size() {
      return this.list.size();
   }

   public int pages() {
      return (int)Math.ceil((double)this.size() / (double)this.perPage);
   }

   public int page() {
      int var1 = this.pages();
      if (var1 == 0) {
         return 0;
      } else {
         return this.page >= var1 ? var1 - 1 : this.page;
      }
   }

   public int fromIndex() {
      return this.page() * this.perPage;
   }

   public int toIndex() {
      return Math.min(this.fromIndex() + this.perPage, this.size());
   }

   public List<T> sublist() {
      return this.list.subList(this.fromIndex(), this.toIndex());
   }

   public List<T> list() {
      return this.list;
   }
}
