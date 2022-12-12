package com.example.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/opload", produces = {"application/json"})
@Slf4j
@CrossOrigin
public class UploadArquivoController {

    @PostMapping("/arquivo")
    public ResponseEntity<String> salvarArquivo(@RequestParam("file")MultipartFile file) {
        log.info("Recebendo arquivo", file.getOriginalFilename());
        var path = "/";
        var caminho = path + UUID.randomUUID() + extrairExtensao(file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>("{ \"mansagem:\" \"Arquivo carregado com sucesso!\"}", HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Erro ao processar o arquivo" + ex);
            return new ResponseEntity<>("Erro ao carregar o arquivo!", HttpStatus.BAD_REQUEST);
        }

    }

    private String extrairExtensao(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf(".");
        return nomeArquivo.substring(i + 1);
    }

}
