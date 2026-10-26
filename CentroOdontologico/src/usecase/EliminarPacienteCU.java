package usecase;

import dao.IDao;
import model.Paciente;

public class EliminarPacienteCU {
    public void ejecutar(Integer id, IDao<Paciente> dao) {
        dao.eliminar(id);
    }
}