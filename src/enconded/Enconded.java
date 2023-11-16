package enconded;

public class Enconded {

    public static void main(String[] args)  {
        TesteService testeService = new TesteService();
        Entity entity = new Entity(11L, "José", 61);
        Criptografar criptografar = testeService.criptografar(entity);
        Entity en = testeService.descriptografar(criptografar);
        System.out.println(en.getName());
    }

}
