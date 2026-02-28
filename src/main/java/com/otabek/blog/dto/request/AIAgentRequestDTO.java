package com.otabek.blog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIAgentRequestDTO {

    private String name;
    private String description;
    private String agentType;
    private String apiProvider;
    private String version;
    private String capabilities;
    private List<String> supportedModels;
    private String configuration;
    private Boolean isActive;
}
