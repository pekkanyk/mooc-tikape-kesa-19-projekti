/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varausjarjestelma;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author qru19
 */
public interface Dao <T, K>{
    T create (T object) throws SQLException;
    T read (K key) throws SQLException;
    List<T> list() throws SQLException;
}
