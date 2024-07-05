package com.gmail.holubvojtech.jsql.mapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class ValueMapper<T> {
   public static final ValueMapper VOID_MAPPER = new ValueMapper() {
      public ValueMapper add(Object var1) {
         return this;
      }

      public ValueMapper addAll(Object var1) {
         return this;
      }

      public ValueMapper addAll(ValueMapper var1) {
         return this;
      }

      public void apply(Object var1, int var2) {
      }
   };
   protected List<Object> list = new ArrayList();

   public ValueMapper add(Object var1) {
      this.list.add(var1);
      return this;
   }

   public ValueMapper addAll(Object var1) {
      if (var1 instanceof Collection) {
         Collection var5 = (Collection)var1;
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            Object var4 = var6.next();
            this.add(var4);
         }

         return this;
      } else if (!var1.getClass().isArray()) {
         throw new IllegalArgumentException("object must be Collection or Array");
      } else {
         int var2 = Array.getLength(var1);

         for(int var3 = 0; var3 < var2; ++var3) {
            this.add(Array.get(var1, var3));
         }

         return this;
      }
   }

   public ValueMapper addAll(ValueMapper var1) {
      this.list.addAll(var1.list);
      return this;
   }

   public void apply(T var1) {
      this.apply(var1, 0);
   }

   public abstract void apply(T var1, int var2);
}
