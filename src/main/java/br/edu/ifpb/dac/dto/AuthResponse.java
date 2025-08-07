package br.edu.ifpb.dac.dto;

import java.util.List;

public record AuthResponse(String accessToken, List<String> roles) { }