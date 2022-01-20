package com.xzy.springboot.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.xzy.springboot.exception.ResourceNotFoundException;
import com.xzy.springboot.model.Item;
import com.xzy.springboot.repository.ItemRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/InventoryApp/")
public class ItemController {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping("/items")
	public List<Item> getAllItems() {
		return itemRepository.findAll();
	}
	
	@PostMapping("/items")
	public Item createItem(@RequestBody Item item) {
		return itemRepository.save(item);
	}
	
	@GetMapping("/items/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item with id " + id + " does not exist!"));
		return ResponseEntity.ok(item);
	}
	
	@PutMapping("/items/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemUpdate) {
		Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item with id " + id + " does not exist!"));
		item.setName(itemUpdate.getName());
		item.setAmount(itemUpdate.getAmount());
		item.setLocation(itemUpdate.getLocation());
		Item updatedItem = itemRepository.save(item);
		return ResponseEntity.ok(updatedItem);
	}
	
	@DeleteMapping("/items/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item with id " + id + " does not exist!"));
		itemRepository.delete(item);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", true);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
         
        List<Item> listItems = itemRepository.findAll();;
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Name", "Amount", "Location"};
        String[] nameMapping = {"id", "name", "amount", "location"};
         
        csvWriter.writeHeader(csvHeader);
         
        for (Item user : listItems) {
            csvWriter.write(user, nameMapping);
        }
         
        csvWriter.close();
         
    }
}
