package usecase;

import dao.IDao;
import model.Paciente;

public class BuscarPacienteCU {
    public Paciente ejecutar(Integer id, IDao<Paciente> dao) {
        return dao.buscar(id);
    }
}