
package enconded;

import java.util.List;

public interface GenericRepository<T> {
    void save(String caminhoPasta, String nomeArquivo, T entity);
    T findBy(String keyCaminho, String keyNomeArquivo, Long id);
    List<T> findAll(String keyCaminho, String keyNomeArquivo);
    void delete(String keyCaminho, String keyNomeArquivo, Long id);
    Criptografar criptografar(T entity);
    T descriptografar(Criptografar criptografar);
}
