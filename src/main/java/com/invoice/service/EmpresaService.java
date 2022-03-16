package com.invoice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invoice.model.Empresa;
import com.invoice.repository.EmpresaRepository;
import com.invoice.util.SHA512CryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final UserService userService;

    private final EmpresaRepository repository;

    public Empresa findByUserId(Long userId){
		return this.repository.findByUserId(userId);
	}

    public Empresa save(Empresa Empresa){
        return saveEmpresa(Empresa);
    }

    private Empresa saveEmpresa(Empresa Empresa) {
        Empresa.getUser().setPermissions(new ArrayList<>());
        Empresa.getUser().setPassword(SHA512CryptPasswordEncoder.builder().build().encode(Empresa.getUser().getPassword()));
        Empresa.setUser(userService.createUser(Empresa.getUser()));
        return repository.save(Empresa);
    }

    public Empresa findById(Long id) {
        Empresa empresa = repository.findById(id).get();
        if(empresa.getLogotipo() != null)
            empresa.setLogotipoBase64(Base64.getEncoder().encodeToString(empresa.getLogotipo()));

        return empresa;
    }

    public Empresa findUserByToken() throws JsonProcessingException, IllegalArgumentException {
        return findById(Long.parseLong(MDC.get("empresa")));
    }

    public String saveLogotipo(MultipartFile multipartFile) throws IOException {
        Empresa empresa = repository.findById(Long.parseLong(MDC.get("empresa"))).get();
        empresa.setLogotipo(multipartFile.getInputStream().readAllBytes());
        repository.save(empresa);
        return Base64.getEncoder().encodeToString(empresa.getLogotipo());
    }

    public Empresa saveInformation(String information) {
        Empresa empresa = repository.findById(Long.parseLong(MDC.get("empresa"))).get();
        empresa.setInformacoes(information);
        empresa = repository.save(empresa);
        return empresa;
    }
}
