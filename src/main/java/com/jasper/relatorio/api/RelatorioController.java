package com.jasper.relatorio.api;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jasper.relatorio.service.RelatorioService;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/vendas")
    public ResponseEntity<byte[]> imprimirCotacao() {

        byte[] pdfBytes = relatorioService.gerarImpressoVendas();

        if (Objects.isNull(pdfBytes)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        /*
         * Realiza o download direto do back para fins de teste
         * o melhor seria converter em base64 ou enviar o byte[] para o front fazer o download do arquivo
        */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-vendas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
