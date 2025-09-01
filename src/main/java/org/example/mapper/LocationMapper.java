package org.example.mapper;

import org.example.dto.LocationResponseDto;
import org.example.model.Location;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(imports = BigDecimal.class)
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "latitude", expression = "java(BigDecimal.valueOf(dto.lat()))")
    @Mapping(target = "longitude", expression = "java(BigDecimal.valueOf(dto.lon()))")
    Location toEntity(LocationResponseDto dto);


    @Mapping(target = "lat", expression = "java(location.getLatitude().doubleValue())")
    @Mapping(target = "lon", expression = "java(location.getLongitude().doubleValue())")
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    LocationResponseDto toDto(Location location);



    default Location toEntityWithUser(LocationResponseDto dto, User user) {
        Location location = toEntity(dto);
        location.setUser(user);
        return location;
    }
}
