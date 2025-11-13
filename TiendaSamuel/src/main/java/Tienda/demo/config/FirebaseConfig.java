/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda.demo.config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public Storage storage() throws IOException {
        System.out.println("=== INICIANDO CONFIGURACIÓN FIREBASE ===");
        
        // Lista de posibles ubicaciones (incluyendo subcarpeta firebase)
        String[] possibleFilePaths = {
            "tienda-samuel-techshop-firebase-adminsdk-fbsvc-f2d89a2a1d.json", // Raíz
            "firebase/tienda-samuel-techshop-firebase-adminsdk-fbsvc-f2d89a2a1d.json", // Subcarpeta
            "firebase-service-account.json", // Nombre alternativo
            "firebase/firebase-service-account.json" // Subcarpeta con nombre alternativo
        };
        
        InputStream serviceAccountStream = null;
        String foundFilePath = null;
        
        for (String filePath : possibleFilePaths) {
            Resource resource = new ClassPathResource(filePath);
            if (resource.exists()) {
                System.out.println("✅ Archivo encontrado: " + filePath);
                serviceAccountStream = resource.getInputStream();
                foundFilePath = filePath;
                break;
            } else {
                System.out.println("❌ Archivo no encontrado: " + filePath);
            }
        }
        
        if (serviceAccountStream == null) {
            // Listar qué archivos hay en resources/firebase para debug
            System.out.println("=== BUSCANDO EN CARPETA FIREBASE ===");
            try {
                java.nio.file.Path firebasePath = java.nio.file.Paths.get("src/main/resources/firebase");
                if (java.nio.file.Files.exists(firebasePath)) {
                    System.out.println("Contenido de src/main/resources/firebase:");
                    java.nio.file.Files.list(firebasePath)
                            .forEach(path -> System.out.println(" - " + path.getFileName()));
                } else {
                    System.out.println("La carpeta firebase no existe en resources");
                }
            } catch (Exception e) {
                System.out.println("Error al listar archivos de firebase: " + e.getMessage());
            }
            
            throw new RuntimeException("""
                ❌ CRÍTICO: No se encontró el archivo de credenciales de Firebase.
                
                OPCIONES:
                1. Mueve tu archivo JSON a: src/main/resources/tienda-samuel-techshop-firebase-adminsdk-fbsvc-f2d89a2a1d.json
                2. O colócalo en: src/main/resources/firebase/tienda-samuel-techshop-firebase-adminsdk-fbsvc-f2d89a2a1d.json
                3. O renómbralo a: firebase-service-account.json
                
                La estructura actual de resources es:
                • application.properties
                • firebase/ (carpeta)
                • messages.properties
                • etc...
                """);
        }
        
        try {
            System.out.println("✅ Cargando credenciales desde: " + foundFilePath);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
            
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
            
            System.out.println("✅ Cliente de Firebase Storage creado exitosamente");
            System.out.println("✅ Bucket configurado: tienda-samuel-techshop.appspot.com");
            return storage;
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear cliente Firebase: " + e.getMessage());
            throw e;
        } finally {
            if (serviceAccountStream != null) {
                serviceAccountStream.close();
            }
        }
    }
}