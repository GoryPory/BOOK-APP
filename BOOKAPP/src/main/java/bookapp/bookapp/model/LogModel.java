/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import java.util.List;

public class LogModel implements BaseModel<LogModel, Integer> {

    @Override
    public List<LogModel> readAll() {
        // TODO: Implementasi SELECT * FROM log_inventori
        return null; 
    }

    @Override
    public List<LogModel> search(String keyword) {
        // TODO: (SURE/Opsional) Implementasi pencarian log
        return null;
    }
}