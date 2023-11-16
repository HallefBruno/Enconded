package enconded;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class GenericService<T> implements GenericRepository<T> {
    
    private final Util<T> util;

    public GenericService() {
        this.util = new Util<>();
    }

    @Override
    public void save(String keyCaminho, String keyNomeArquivo, T entity) {
        try {
            util.writeData(keyCaminho, keyNomeArquivo, entity);
        } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T findBy(String keyCaminho, String keyNomeArquivo, Long id) {
        try {
            return util.get(keyCaminho, keyNomeArquivo, id);
        } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<T> findAll(String keyCaminho, String keyNomeArquivo) {
        try {
            return util.findAll(keyCaminho, keyNomeArquivo);
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String keyCaminho, String keyNomeArquivo, Long id) {
        try {
            util.remove(keyCaminho, keyNomeArquivo, id);
        } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Criptografar criptografar(T entity) {
        try {
            return util.encript(entity);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IOException | BadPaddingException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T descriptografar(Criptografar criptografar) {
        try {
            return util.decript(criptografar);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | IOException | IllegalBlockSizeException | ClassNotFoundException | BadPaddingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
