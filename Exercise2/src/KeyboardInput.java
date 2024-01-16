import java.util.Scanner;
/**
 * A singleton class for reading from the keyboard.
 * The Singleton pattern ensures that a class has only one instance and provides a global point
 * of access to that instance.
 */
class KeyboardInput
{
    private static KeyboardInput keyboardInputObject = null;
    private Scanner scanner;

    private KeyboardInput()
    {
        this.scanner = new Scanner(System.in);
    }

    /*
    * This is the global access point for getting the single instance of the KeyboardInput class.
    * If the instance doesn't exist (keyboardInputObject == null), it creates one. Otherwise, it
    * returns the existing instance.
    */
    public static KeyboardInput getObject()
    {
        if(KeyboardInput.keyboardInputObject == null)
        {
            KeyboardInput.keyboardInputObject = new KeyboardInput();
        }
        return KeyboardInput.keyboardInputObject;
    }

    public static int readInt()
    {
        return KeyboardInput.getObject().scanner.nextInt();
    }
}