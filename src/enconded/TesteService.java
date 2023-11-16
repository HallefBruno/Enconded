package enconded;

import java.util.List;

public class TesteService extends GenericService<Entity> {

    @Override
    public void save(String keyCaminho, String keyNomeArquivo, Entity entity) {
        super.save(keyCaminho, keyNomeArquivo, entity);
    }

    @Override
    public Entity findBy(String keyCaminho, String keyNomeArquivo, Long id) {
        return super.findBy(keyCaminho, keyNomeArquivo, id);
    }

    @Override
    public void delete(String keyCaminho, String keyNomeArquivo, Long id) {
        super.delete(keyCaminho, keyNomeArquivo, id);
    }

    @Override
    public List<Entity> findAll(String keyCaminho, String keyNomeArquivo) {
        return super.findAll(keyCaminho, keyNomeArquivo);
    }

    @Override
    public Criptografar criptografar(Entity entity) {
        return super.criptografar(entity);
    }
    
    @Override
    public Entity descriptografar(Criptografar criptografar) {
        return super.descriptografar(criptografar);
    }
    
}
