package com.sid.controller;

import com.sid.Repos.CustomRepo;
import com.sid.model.Customer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

@Controller
public class CustomerController {
    private final CustomRepo customerRepo;

    @Autowired
    public CustomerController(CustomRepo customer) {
        this.customerRepo = customer;
    }

    @GetMapping("/insertExcel")
    public String insertExcel(RedirectAttributes redirectAttributes){
        ArrayList<String> values = new ArrayList<>();
        //int count;
        try{
             //recuperation file excel
            InputStream input = new FileInputStream("customer.xls");
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            Iterator rows = sheet.rowIterator();

            //to iterate
            while(rows.hasNext()){
                values.clear();
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                //to iterate cell
                while(cells.hasNext()){
                    HSSFCell cell = (HSSFCell) cells.next();
                    if(HSSFCell.CELL_TYPE_NUMERIC==cell.getCellType())
                        values.add(Integer.toString((int)cell.getNumericCellValue()));
                    else if(HSSFCell.CELL_TYPE_STRING==cell.getCellType())
                        values.add(cell.getStringCellValue());
                }
                //insert data to customer
                Customer customer = new Customer();
                customer.setName(values.get(0));
                customer.setFirstname(values.get(1));
                customer.setTelephone(values.get(2));
                customerRepo.save(customer);
                System.out.println("enregistrement effectué");
                redirectAttributes.addFlashAttribute("message","SuccessFully added!!!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //insert data

        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }

}
