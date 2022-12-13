package com.example.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/upload", produces = {"application/json"})
@Slf4j
@CrossOrigin("*")
public class UploadArquivoController {

    private final String pathArquivos;

    public UploadArquivoController(@Value("${app.path.arquivos}") String pathArquivos) {

        this.pathArquivos = pathArquivos;

    }

    @PostMapping("/arquivo")
    public ResponseEntity<String> salvarArquivo(@RequestParam("file") @NotNull MultipartFile file) {
        log.info("Recebendo arquivo", file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        String basename = extrairBasename(filename);
        String extensao = extrairExtensao(filename);

        try {
            File tempFile = File.createTempFile(basename + '-', '.' + extensao);
            tempFile.deleteOnExit();
            log.info("Novo nome do arquivo: " + tempFile.getPath());
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("{ \"mansagem:\" \"Arquivo carregado com sucesso!\"}");
        } catch (SizeLimitExceededException sizeLimitExceededException) {
            log.error("Erro ao carregar o arquivo " + sizeLimitExceededException);
            return ResponseEntity.badRequest().body("Tamanho do arquivo maior que o suportado!");
        } catch (MaxUploadSizeExceededException maxUploadSizeExceededException) {
            log.error("Erro ao processar o arquivo " + maxUploadSizeExceededException);
            return ResponseEntity.badRequest().body("Tamanho do arquivo maior que o suportado!");
        } catch (Exception exception) {
            log.error("Erro ao processar o arquivo " + exception);
            return ResponseEntity.badRequest().body("Erro ao carregar o arquivo!");
        }

    }

    private String extrairBasename(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf(".");
        return nomeArquivo.substring(0, i);
    }

    private String extrairExtensao(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf(".");
        return nomeArquivo.substring(i + 1);
    }

}
