package com.example.alexpineda.launch27.WebServices.Dto;

import java.util.List;

/**
 * Created by alexpineda77 on 2015-11-03.
 */
public class SpotDtoResponse {
    public int id;
    public int hours;
    public int minutes;
    public String service_date;
    public int value;
    public int remaining;
    public List<SpotFlexibilityDtoResponse> spot_flexibilities;

}