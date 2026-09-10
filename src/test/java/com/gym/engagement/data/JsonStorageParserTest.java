package com.gym.engagement.data;

import com.gym.engagement.data.dto.StorageInitializationDTO;
import com.gym.engagement.data.impl.JsonStorageParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonStorageParserTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private JsonStorageParser parser;

    @BeforeEach
    void setUp() {
        parser = new JsonStorageParser(resourceLoader);
    }

    @Test
    void parseSourceData_shouldReturnPopulatedDTO_whenJsonIsValid() throws IOException {
        String json = """
                {
                  "trainees": [
                    {
                      "userId": 101,
                      "firstName": "John",
                      "lastName": "Pork",
                      "userName": "john.pork",
                      "password": "Password123!",
                      "isActive": true,
                      "dateOfBirth": "1998-05-15",
                      "address": "123 Main St, New York, NY",
                      "trainings": []
                    }
                  ],
                  "trainers": [],
                  "trainings": []
                }
                """;

        when(resourceLoader.getResource("data-source.json")).thenReturn(resource);
        when(resource.getInputStream())
                .thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        StorageInitializationDTO actual = parser.parseSourceData("data-source.json");

        assertThat(actual.getTrainees()).hasSize(1);
        assertThat(actual.getTrainees().getFirst().getFirstName()).isEqualTo("John");
        assertThat(actual.getTrainees().getFirst().getUserId()).isEqualTo(101L);
        assertThat(actual.getTrainers()).isEmpty();
        assertThat(actual.getTrainings()).isEmpty();
    }

    @Test
    void parseSourceData_shouldThrowIllegalStateException_whenJsonIsMalformed() throws IOException {
        String malformedJson = "{ sdfjlsd ";

        when(resourceLoader.getResource("bad.json")).thenReturn(resource);
        when(resource.getInputStream())
                .thenReturn(new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8)));

        assertThatThrownBy(() -> parser.parseSourceData("bad.json"))
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void parseSourceData_shouldReturnEmptyLists_whenJsonHasNoEntities() throws IOException {
        String json = """
                {
                  "trainees": [],
                  "trainers": [],
                  "trainings": []
                }
                """;

        when(resourceLoader.getResource("empty.json")).thenReturn(resource);
        when(resource.getInputStream())
                .thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        StorageInitializationDTO actual = parser.parseSourceData("empty.json");

        assertThat(actual.getTrainees()).isEmpty();
        assertThat(actual.getTrainers()).isEmpty();
        assertThat(actual.getTrainings()).isEmpty();
    }

}
