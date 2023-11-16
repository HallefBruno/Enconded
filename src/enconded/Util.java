package enconded;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Util<T> {

    public Util() {
    }
    
    private java.util.logging.Logger LOGGER =  java.util.logging.Logger.getLogger(this.getClass().getName());
    private static final Properties PROPERTIES = new Properties();
    private static final String PATHFILECONFIG = "src/enconded/configuration.properties";
    private static String EXTENSION = null;

    static {
        try (InputStream input = new FileInputStream(PATHFILECONFIG)) {
            if (input == null) {
                throw new RuntimeException("Desculpe, não foi possível encontrar configuration.properties");
            }
            PROPERTIES.load(input);
            EXTENSION = PROPERTIES.getProperty("extension");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void createFile(String keyCaminho, String keyNomeArquivo) throws IOException {
        File file = new File(keyCaminho);
        file.mkdir();
        if (file.exists()) {
            File arq = new File(file, keyNomeArquivo);
            if (!arq.exists()) {
                arq.createNewFile();
                file.setExecutable(false);
                file.setReadable(false);
                file.setWritable(false);
                LOGGER.info("Arquivo criado!");
            }
        }
    }

    public void writeData(String keyCaminho, String keyNomeArquivo, T entity) throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        String pathFileProperties = PROPERTIES.getProperty(keyCaminho) + PROPERTIES.getProperty(keyNomeArquivo) + EXTENSION;
        createFile(PROPERTIES.getProperty(keyCaminho), PROPERTIES.getProperty(keyNomeArquivo) + EXTENSION);
        if (get(keyCaminho, keyNomeArquivo, getId(entity)) == null) {
            if (EXTENSION.toLowerCase().equalsIgnoreCase(".properties")) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(pathFileProperties));
                prop.setProperty(getId(entity).toString(), serialize(entity));
                prop.store(new FileOutputStream(pathFileProperties), null);
                LOGGER.info("Dado salvo com sucesso!");
            } else {
                throw new RuntimeException("Extensão desconhecida!");
            }
        } else {
            throw new RuntimeException("Já existe um registro com esse identificador!");
        }
    }
    
    public List<T> findAll(String keyCaminho, String keyNomeArquivo) throws FileNotFoundException, IOException, ClassNotFoundException {
        checkKey(keyCaminho);
        String extension = PROPERTIES.getProperty("extension");
        String path = PROPERTIES.getProperty(keyCaminho) + PROPERTIES.getProperty(keyNomeArquivo) + extension;
        List<T> list = new ArrayList<>();
        if (extension.toLowerCase().equalsIgnoreCase(".properties")) {
            Properties prop = new Properties();
            prop.load(new FileInputStream(path));
            LOGGER.info("Recuperando todos os registros!");
            for (Map.Entry<Object, Object> e : prop.entrySet()) {
                T entity = deserialize(e.getValue().toString());
                list.add(entity);
            }
        } else {
            throw new RuntimeException("Extensão desconhecida!");
        }
        return list;
    }

    public T get(String keyCaminho, String keyNomeArquivo, Long id) throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        checkKey(keyCaminho);
        String path = PROPERTIES.getProperty(keyCaminho) + PROPERTIES.getProperty(keyNomeArquivo) + EXTENSION;
        if (EXTENSION.toLowerCase().equalsIgnoreCase(".properties")) {
            Properties prop = new Properties();
            prop.load(new FileInputStream(path));
            for (Map.Entry<Object, Object> e : prop.entrySet()) {
                T entity = deserialize(e.getValue().toString());
                Field field = entity.getClass().getDeclaredField("id");
                field.setAccessible(true);
                Long idValue = Long.valueOf(field.get(entity).toString());
                if (idValue.longValue() == id.longValue()) {
                    LOGGER.info("Registro encontrado!");
                    return entity;
                }
            }
        } else {
            throw new RuntimeException("Extensão desconhecida!");
        }
        return null;
    }
    
    public void remove(String keyCaminho, String keyNomeArquivo, Long id) throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        String pathFileProperties = PROPERTIES.getProperty(keyCaminho) + PROPERTIES.getProperty(keyNomeArquivo) + EXTENSION;
        if (EXTENSION.toLowerCase().equalsIgnoreCase(".properties")) {
            if(get(keyCaminho, keyNomeArquivo, id) != null) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(pathFileProperties));
                prop.remove(id.toString());
                prop.store(new FileOutputStream(pathFileProperties), null);
                LOGGER.info("Dado removido com sucesso!");
            } else {
                throw new RuntimeException("Id não encontrado!");
            }
        } else {
            throw new RuntimeException("Extensão desconhecida!");
        }
    }

    private Long readKey(String keyCaminho) throws FileNotFoundException, IOException, ClassNotFoundException {
        BufferedReader bufferedReader;
        Long key = 0L;
        createFile(keyCaminho, "key.txt");

        try (FileReader fileReader = new FileReader(keyCaminho + "key.txt")) {
            bufferedReader = new BufferedReader(fileReader);
            String dados;
            while ((dados = bufferedReader.readLine()) != null) {
                key = Long.valueOf(dados);
            }
        }
        bufferedReader.close();
        key++;

        FileWriter fileWriter = new FileWriter(keyCaminho + "key.txt");
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(key);
            printWriter.flush();
        }
        return key;
    }

    private String serialize(T entity) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(entity);
        return new String(Base64.getEncoder().encode(out.toByteArray()));
    }

    private T deserialize(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        ObjectInputStream is = new ObjectInputStream(in);
        return (T) is.readObject();
    }

    private Long getId(T entity) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        if(field.get(entity) == null) {
            throw new RuntimeException("Identificador não pode ser null!");
        }
        Long idValue = Long.valueOf(field.get(entity).toString());
        
        if(idValue < 0) {
            throw new RuntimeException("Identificador não pode ser menor que zero!");
        }
        return idValue;
    }
    
    private void checkKey(String keyCaminho) {
        LOGGER.info("Verificando se existe o registro!");
        if(PROPERTIES.getProperty(keyCaminho) == null) {
            throw new RuntimeException("A chave não existe");
        }
    }
    
    public Criptografar encript(T entity) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, BadPaddingException, ClassNotFoundException {
        Criptografar criptografar = new Criptografar();
        
        SecretKey key = Encryption.generateKey(128);
        IvParameterSpec ivParameterSpec = Encryption.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";
        
        criptografar.setIvParameterSpec(ivParameterSpec);
        criptografar.setAlgorithm(algorithm);
        criptografar.setKey(key);
        criptografar.setSealedObject(Encryption.encryptObject(criptografar.getAlgorithm(), ((Serializable) entity), criptografar.getKey(), criptografar.getIvParameterSpec()));
        
        return criptografar;
    }
    
    public T decript(Criptografar criptografar) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, ClassNotFoundException, BadPaddingException {
        //System.out.println(Base64.getEncoder().encodeToString(sealedObject.toString().getBytes()));
        return (T) Encryption.decryptObject(criptografar.getAlgorithm(), criptografar.getSealedObject(), criptografar.getKey(), criptografar.getIvParameterSpec());
    }
    
    private void setPermission(File file) throws IOException {
        Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rwxrwxrwx");
        FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(ownerWritable);
        Files.createFile(file.toPath(), permissions);
//        Set<PosixFilePermission> perms = new HashSet<>();
//        perms.add(PosixFilePermission.OWNER_READ);
//        perms.add(PosixFilePermission.OWNER_WRITE);
//        perms.add(PosixFilePermission.OWNER_EXECUTE);
//
//        perms.add(PosixFilePermission.OTHERS_READ);
//        perms.add(PosixFilePermission.OTHERS_WRITE);
//        perms.add(PosixFilePermission.OTHERS_EXECUTE);
//
//        perms.add(PosixFilePermission.GROUP_READ);
//        perms.add(PosixFilePermission.GROUP_WRITE);
//        perms.add(PosixFilePermission.GROUP_EXECUTE);
//
//        Files.setPosixFilePermissions(file.toPath(), perms);
    }

}
