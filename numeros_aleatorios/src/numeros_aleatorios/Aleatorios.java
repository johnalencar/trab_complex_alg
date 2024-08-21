
package numeros_aleatorios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Aleatorios {
    public static void main(String[] args) {
        String fileName = "numeros_1B.txt";
        int numberOfLines = 1_000_000_000;
        Random random = new Random();
        
     // Inicializa o monitoramento de sistema
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        Runtime runtime = Runtime.getRuntime();

        // Medindo o tempo de execução
        long startTime = System.nanoTime();

        // Medindo a memória antes de carregar o arquivo
        long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();
        double initialCpuLoad = osBean.getProcessCpuLoad() * 100;
        

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < numberOfLines; i++) {
                int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1; // Gera um número aleatório maior que zero
                writer.write(String.valueOf(randomNumber));
                writer.newLine();
            }
            System.out.println("Arquivo gerado com sucesso: " + fileName);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
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
}
