import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedasAPI {

    private static final String API_KEY = "8fdba2b395400c5f9922e151";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();

            switch(opcion) {
                case 1:
                    convertirMoneda("USD", "ARS", scanner);
                    break;
                case 2:
                    convertirMoneda("ARS", "USD", scanner);
                    break;
                case 3:
                    convertirMoneda("USD", "BRL", scanner);
                    break;
                case 4:
                    convertirMoneda("BRL", "USD", scanner);
                    break;
                case 5:
                    convertirMoneda("USD", "COP", scanner);
                    break;
                case 6:
                    convertirMoneda("COP", "USD", scanner);
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, intente nuevamente.");
            }

        } while(opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n*** Sea Bienvenido Al Conversor De Monedas ***");
        System.out.println("1. Dólar            =>>   Peso Argentino");
        System.out.println("2. Peso Argentino   =>>   Dólar");
        System.out.println("3. Dólar            =>>   Real Brasileño");
        System.out.println("4. Real Brasileño   =>>   Dólar");
        System.out.println("5. Dólar            =>>   Peso Colombiano");
        System.out.println("6. Peso Colombiano  =>>   Dólar");
        System.out.println("7. Salir");
        System.out.println("Elija una opción válida: ");
        System.out.println("**********************************************");
    }

    private static void convertirMoneda(String desde, String hacia, Scanner scanner) {
        try {
            double tasa = obtenerTasaConversion(desde, hacia);

            System.out.printf("Ingrese la cantidad en %s: ", desde);
            double cantidad = scanner.nextDouble();
            double resultado = cantidad * tasa;

            System.out.printf("%.2f %s = %.2f %s%n", cantidad, desde, resultado, hacia);
        } catch (Exception e) {
            System.out.println("Error al obtener tasas de cambio: " + e.getMessage());
        }
    }

    private static double obtenerTasaConversion(String desde, String hacia) throws Exception {
        String urlString = BASE_URL + API_KEY + "/pair/" + desde + "/" + hacia;
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error en la API: " + conn.getResponseMessage());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getDouble("conversion_rate");
    }
}