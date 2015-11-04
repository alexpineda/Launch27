package com.example.alexpineda.launch27.WebServices.Dto;

import java.util.List;

/**
 * Created by alexpineda77 on 2015-11-03.
 */
public class ServiceDtoResponse {
    public int id;
    public String code;
    public double price;
    public String name;
    public boolean hourly;
    public int maids_minimum;
    public int maids_maximum;
    public int hours_minimum;
    public int hours_maximum;
    public List<ExtraDtoResponse> extras;
    public List<PriceParameterDtoResponse> pricing_parameters;

}
