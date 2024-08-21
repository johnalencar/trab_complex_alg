package procurar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Procurar {
    public static void main(String[] args) {
        String fileName = "numeros_1B.txt"; // Nome do arquivo a ser lido
        long targetNumber = 441649437; // Número que você deseja encontrar

        // Inicializa o monitoramento de sistema
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        Runtime runtime = Runtime.getRuntime();

        // Medindo o tempo de execução
        long startTime = System.nanoTime();

        // Medindo a memória antes de carregar o arquivo
        long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();
        double initialCpuLoad = osBean.getProcessCpuLoad() * 100;

        try {
            // Conte o número de linhas no arquivo para inicializar o array
            long lineCount = countLinesInFile(fileName);

            // Crie um array para armazenar os números
            long[] numbers = new long[(int) lineCount];

            // Carregue o arquivo para o array
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                int index = 0;

                while ((line = reader.readLine()) != null) {
                    numbers[index++] = Long.parseLong(line);
                }
            }

            // Procure o número dentro do array
            boolean found = false;
            int foundIndex = -1;

            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] == targetNumber) {
                    found = true;
                    foundIndex = i;
                    break;
                }
            }

            if (found) {
                System.out.println("Número " + targetNumber + " encontrado no índice " + foundIndex);
            } else {
                System.out.println("Número " + targetNumber + " não encontrado no array.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao formatar o número: " + e.getMessage());
        }

        // Medindo o tempo de execução
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // em milissegundos

        // Medindo a memória após carregar o arquivo
        long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMemory - beforeUsedMemory;
        double finalCpuLoad = osBean.getProcessCpuLoad() * 100;

        // Exibindo as métricas
        System.out.println("Tempo de execução: " + duration + " ms");
        System.out.printf("Uso de Memória RAM: %.2f MB\n", memoryUsed / (1024.0 * 1024.0));
        System.out.printf("Uso de CPU inicial: %.2f%%\n", initialCpuLoad);
        System.out.printf("Uso de CPU final: %.2f%%\n", finalCpuLoad);
    }

    // Método auxiliar para contar as linhas em um arquivo
    private static long countLinesInFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.lines().count();
        }
    }
}

