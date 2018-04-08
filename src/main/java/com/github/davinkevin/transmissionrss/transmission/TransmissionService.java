package com.github.davinkevin.transmissionrss.transmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davinkevin.transmissionrss.transmission.arguments.AddTorrentArguments;
import com.github.davinkevin.transmissionrss.transmission.properties.ServerProperty;
import com.github.davinkevin.transmissionrss.transmission.request.AddTorrentRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Function;

import static io.vavr.API.Try;

/**
 * Created by kevin on 08/04/2018
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransmissionService {

    private static final String X_TRANSMISSION_SESSION_ID = "X-Transmission-Session-Id";

    private final ObjectMapper mapper;
    private final ServerProperty serverProperty;

    @Getter private String token;

    public void add(AddTorrentArguments arguments) {
        Try(() -> Unirest.post(generateUrl())
                .header(X_TRANSMISSION_SESSION_ID, token)
                .body(new AddTorrentRequest<>(arguments))
                .asString()
        )
                .onFailure(e -> log.error("Error during add", e))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);

    }

    @PostConstruct
    public void setup() {
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            public <T> T readValue(String value, Class<T> valueType) {
                return Try(() -> mapper.readValue(value, valueType))
                        .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
            }

            public String writeValue(Object value) {
                return Try(() -> mapper.writeValueAsString(value))
                        .onSuccess(log::debug)
                        .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
            }
        });
    }

    @PostConstruct
    private void fetchToken() {
        token = Try(() -> Unirest.post(generateUrl()).asBinary())
                .map(HttpResponse::getHeaders)
                .map(HashMap::ofAll)
                .getOrElse(HashMap.empty())
                .find(v -> v._1().equals(X_TRANSMISSION_SESSION_ID))
                .map(Tuple2::_2)
                .map(List::ofAll)
                .getOrElse(List::empty)
                .headOption()
                .getOrElseThrow(() -> new RuntimeException("No X-Transmission-Session-Id header found"))
                ;
    }

    private String generateUrl() {
        return "http://"+ serverProperty.getHost() + ":" + serverProperty.getPort() + "/" + serverProperty.getRpcPath();
    }
}
