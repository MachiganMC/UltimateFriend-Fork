package com.gmail.holubvojtech.jsql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementSetter {
   void apply(PreparedStatement var1) throws SQLException;
}
