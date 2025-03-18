package indimetra.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.repository.ICortometrajeRepository;

@Service
public class CortometrajeServiceImplMy8 extends GenericoCRUDServiceImplMy8<Cortometraje, Long>
        implements ICortometrajeService {

    @Autowired
    private ICortometrajeRepository cortometrajeRepository;

    @Override
    protected ICortometrajeRepository getRepository() {
        return cortometrajeRepository;
    }

    @Override
    public Optional<Cortometraje> findByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }

    @Override
    public List<Cortometraje> findByCategory(String category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCategory'");
    }

    @Override
    public List<Cortometraje> findByRating(Double rating) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByRating'");
    }

    @Override
    public Optional<Cortometraje> findByTitle(String title) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTitle'");
    }

    @Override
    public List<Cortometraje> findLatestSeries() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findLatestSeries'");
    }
}
