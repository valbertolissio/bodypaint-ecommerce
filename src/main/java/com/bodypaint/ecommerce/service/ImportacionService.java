package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.Producto;
import com.bodypaint.ecommerce.repository.ProductoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@Service
public class ImportacionService {

    private final ProductoRepository productoRepository;

    public ImportacionService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Map<String, Object> importarCSV(MultipartFile file) throws Exception {
        List<String> nuevos = new ArrayList<>();
        List<String> actualizados = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord record : parser) {
                String nombre = record.get("nombre");
                String marca = record.get("marca");
                int stock = Integer.parseInt(record.get("stock").trim());
                double precio = Double.parseDouble(record.get("precio").trim());

                Optional<Producto> existente = productoRepository.findAll()
                    .stream().filter(p -> p.getNombre().equalsIgnoreCase(nombre)).findFirst();

                if (existente.isPresent()) {
                    Producto p = existente.get();
                    p.setStock(stock);
                    p.setPrecio(precio);
                    p.setMarca(marca);
                    productoRepository.save(p);
                    actualizados.add(nombre);
                } else {
                    Producto p = new Producto();
                    p.setNombre(nombre);
                    p.setMarca(marca);
                    p.setStock(stock);
                    p.setPrecio(precio);
                    p.setStockMinimo(0);
                    p.setActivo(true);
                    productoRepository.save(p);
                    nuevos.add(nombre);
                }
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("nuevos", nuevos);
        resultado.put("actualizados", actualizados);
        return resultado;
    }

    public Map<String, Object> importarXLS(MultipartFile file) throws Exception {
        List<String> nuevos = new ArrayList<>();
        List<String> actualizados = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean primeraFila = true;

            for (Row row : sheet) {
                if (primeraFila) { primeraFila = false; continue; }
                if (row.getCell(0) == null) continue;

                String nombre = row.getCell(0).getStringCellValue();
                String marca = row.getCell(1).getStringCellValue();
                int stock = (int) row.getCell(2).getNumericCellValue();
                double precio = row.getCell(3).getNumericCellValue();

                Optional<Producto> existente = productoRepository.findAll()
                    .stream().filter(p -> p.getNombre().equalsIgnoreCase(nombre)).findFirst();

                if (existente.isPresent()) {
                    Producto p = existente.get();
                    p.setStock(stock);
                    p.setPrecio(precio);
                    p.setMarca(marca);
                    productoRepository.save(p);
                    actualizados.add(nombre);
                } else {
                    Producto p = new Producto();
                    p.setNombre(nombre);
                    p.setMarca(marca);
                    p.setStock(stock);
                    p.setPrecio(precio);
                    p.setStockMinimo(0);
                    p.setActivo(true);
                    productoRepository.save(p);
                    nuevos.add(nombre);
                }
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("nuevos", nuevos);
        resultado.put("actualizados", actualizados);
        return resultado;
    }
}
