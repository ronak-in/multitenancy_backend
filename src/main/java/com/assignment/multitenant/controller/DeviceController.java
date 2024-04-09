package com.assignment.multitenant.controller;

import com.assignment.multitenant.bean.DeviceData;
import com.assignment.multitenant.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class DeviceController {

    @Autowired
    private DeviceDataService deviceDataService;

    @RequestMapping(value = "/getdevice/all", method = RequestMethod.GET)
    public ResponseEntity<List<DeviceData>> getAll() throws SQLException {
        List<DeviceData> cities = deviceDataService.getAll();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
