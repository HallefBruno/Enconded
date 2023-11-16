package enconded;

import java.util.Objects;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Criptografar {
    
    private SealedObject sealedObject;
    private IvParameterSpec ivParameterSpec;
    private String algorithm;
    private SecretKey key;

    public Criptografar() {
    }

    public Criptografar(SealedObject sealedObject, IvParameterSpec ivParameterSpec, String algorithm, SecretKey key) {
        this.sealedObject = sealedObject;
        this.ivParameterSpec = ivParameterSpec;
        this.algorithm = algorithm;
        this.key = key;
    }

    public SealedObject getSealedObject() {
        return sealedObject;
    }

    public void setSealedObject(SealedObject sealedObject) {
        this.sealedObject = sealedObject;
    }

    public IvParameterSpec getIvParameterSpec() {
        return ivParameterSpec;
    }

    public void setIvParameterSpec(IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.sealedObject);
        hash = 97 * hash + Objects.hashCode(this.ivParameterSpec);
        hash = 97 * hash + Objects.hashCode(this.algorithm);
        hash = 97 * hash + Objects.hashCode(this.key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Criptografar other = (Criptografar) obj;
        if (!Objects.equals(this.algorithm, other.algorithm)) {
            return false;
        }
        if (!Objects.equals(this.sealedObject, other.sealedObject)) {
            return false;
        }
        if (!Objects.equals(this.ivParameterSpec, other.ivParameterSpec)) {
            return false;
        }
        return Objects.equals(this.key, other.key);
    }
    
}
