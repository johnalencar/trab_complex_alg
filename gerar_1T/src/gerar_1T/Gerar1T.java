package gerar_1T;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.Random;

public class Gerar1T {
    private static final int BLOCK_SIZE = 100_000_000;  // Tamanho do bloco
    private static final int MAX_NUMBER = Integer.MAX_VALUE;  // Máximo valor do inteiro
    private static final String FILE_PREFIX = "numbers_block_";
    private static final String FILE_SUFFIX = ".dat";

    public static void main(String[] args) {
        long totalNumbers = 1_000_000_000_000L;  // 1 trilhão
        int targetNumber = 441649437;  // Número que você deseja localizar

        // Inicializa medições
        long startTime = System.nanoTime();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        try {
            long numbersGenerated = 0;
            Random random = new Random();

            while (numbersGenerated < totalNumbers) {
                // Determina o tamanho do bloco para este arquivo
                long blockSize = Math.min(BLOCK_SIZE, totalNumbers - numbersGenerated);
                String fileName = FILE_PREFIX + (numbersGenerated / BLOCK_SIZE) + FILE_SUFFIX;

                // Gera e grava o bloco de números aleatórios
                generateAndWriteRandomNumbers(fileName, blockSize, random);

                // Verifica se o número alvo está presente no arquivo
                boolean found = checkNumberInFile(fileName, targetNumber);
                if (found) {
                    System.out.println("Número encontrado: " + targetNumber);
                    // Opcionalmente, você pode interromper a execução se encontrar o número
                    break;
                } else {
                    // Exclui o arquivo se o número não for encontrado
                    new File(fileName).delete();
                }

                numbersGenerated += blockSize;
            }

            System.out.println("Total de números gerados: " + numbersGenerated);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Medições de tempo de execução, CPU e memória
            long endTime = System.nanoTime();
            double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;
            double cpuLoad = osBean.getSystemLoadAverage();
            MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
            double usedMemoryInGB = heapMemoryUsage.getUsed() / (1024.0 * 1024.0 * 1024.0);

            System.out.println("Tempo de execução: " + elapsedTimeInSeconds + " segundos");
            System.out.println("Carga média de CPU: " + cpuLoad);
            System.out.println("Memória usada: " + usedMemoryInGB + " GB");
        }
    }

    private static void generateAndWriteRandomNumbers(String fileName, long blockSize, Random random) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            for (long i = 0; i < blockSize; i++) {
                dos.writeInt(random.nextInt(MAX_NUMBER));
            }
        }
    }

    private static boolean checkNumberInFile(String fileName, int targetNumber) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            while (dis.available() > 0) {
                int number = dis.readInt();
                if (number == targetNumber) {
                    return true;
                }
            }
        }
        return false;
    }
}
