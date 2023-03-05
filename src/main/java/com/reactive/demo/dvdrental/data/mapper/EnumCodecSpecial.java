package com.reactive.demo.dvdrental.data.mapper;

import com.reactive.demo.dvdrental.DictionarySupport;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.r2dbc.postgresql.client.Parameter;
import io.r2dbc.postgresql.codec.Codec;
import io.r2dbc.postgresql.codec.PostgresTypes;
import io.r2dbc.postgresql.extension.CodecRegistrar;
import io.r2dbc.postgresql.message.Format;
import io.r2dbc.postgresql.util.Assert;
import io.r2dbc.postgresql.util.ByteBufUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class EnumCodecSpecial<T extends Enum<T> & DictionarySupport> implements Codec<T> {
    private static final Logger logger = Loggers.getLogger(EnumCodecSpecial.class);
    private final ByteBufAllocator byteBufAllocator;
    private final Class<T> type;
    private final int oid;

    public EnumCodecSpecial(ByteBufAllocator byteBufAllocator, Class<T> type, int oid) {
        this.byteBufAllocator = Assert.requireNonNull(byteBufAllocator, "byteBufAllocator must not be null");
        this.type = (Class) Assert.requireNonNull(type, "type must not be null");
        this.oid = oid;
    }

    public boolean canDecode(int dataType, Format format, Class<?> type) {
        Assert.requireNonNull(type, "type must not be null");
        return type.isAssignableFrom(this.type) && dataType == this.oid;
    }

    public boolean canEncode(Object value) {
        Assert.requireNonNull(value, "value must not be null");
        return this.type.isInstance(value);
    }

    public boolean canEncodeNull(Class<?> type) {
        Assert.requireNonNull(type, "type must not be null");
        return this.type.equals(type);
    }

    @SneakyThrows
    public T decode(@Nullable ByteBuf buffer, int dataType, Format format, Class<? extends T> type) {
        return buffer == null ? null : getReverseByCode(buffer, type);
    }

    private T getReverseByCode(@Nullable ByteBuf buffer, Class<? extends T> type) throws NoSuchMethodException {
        String enumValue = ByteBufUtils.decode(buffer);
        try {
            return Enum.valueOf(this.type, enumValue);
        } catch (IllegalArgumentException e) {
            return DictionarySupport.getByCode(this.type, enumValue);
        }
    }

    public Parameter encode(Object value) {
        Assert.requireNonNull(value, "value must not be null");
        return new Parameter(Format.FORMAT_TEXT, this.oid, Mono.fromSupplier(() -> {
            return ByteBufUtils.encode(this.byteBufAllocator, ((Enum) this.type.cast(value)).name());
        }));
    }

    public Parameter encodeNull() {
        return new Parameter(Format.FORMAT_BINARY, this.oid, Parameter.NULL_VALUE);
    }

    public static EnumCodecSpecial.Builder builder() {
        return new EnumCodecSpecial.Builder();
    }

    public static final class Builder {
        private final Map<String, Class<? extends Enum<?>>> mapping = new LinkedHashMap();
        private EnumCodecSpecial.Builder.RegistrationPriority registrationPriority;

        public Builder() {
            this.registrationPriority = EnumCodecSpecial.Builder.RegistrationPriority.LAST;
        }

        public EnumCodecSpecial.Builder withEnum(String name, Class<? extends Enum<?>> enumClass) {
            Assert.requireNotEmpty(name, "Postgres type name must not be null");
            Assert.requireNonNull(enumClass, "Enum class must not be null");
            Assert.isTrue(enumClass.isEnum(), String.format("Enum class %s must be an enum type", enumClass.getName()));
            if (this.mapping.containsKey(name)) {
                throw new IllegalArgumentException(String.format("Builder contains already a mapping for Postgres type %s", name));
            } else if (this.mapping.containsValue(enumClass)) {
                throw new IllegalArgumentException(String.format("Builder contains already a mapping for Java type %s", enumClass.getName()));
            } else {
                this.mapping.put(name, enumClass);
                return this;
            }
        }

        public EnumCodecSpecial.Builder withRegistrationPriority(EnumCodecSpecial.Builder.RegistrationPriority registrationPriority) {
            this.registrationPriority = (EnumCodecSpecial.Builder.RegistrationPriority) Assert.requireNonNull(registrationPriority, "registrationPriority must not be null");
            return this;
        }

        public CodecRegistrar build() {
            Map<String, Class<? extends Enum<?>>> mapping = new LinkedHashMap(this.mapping);
            return (connection, allocator, registry) -> {
                List<String> missing = new ArrayList(mapping.keySet());
                return PostgresTypes.from(connection).lookupTypes(mapping.keySet()).filter(PostgresTypes.PostgresType::isEnum).doOnNext((it) -> {
                    Class<? extends Enum<?>> enumClass = (Class) mapping.get(it.getName());
                    if (enumClass == null) {
                        EnumCodecSpecial.logger.warn(String.format("Cannot find Java type for enum type '%s' with oid %d. Known types are: %s", it.getName(), it.getOid(), mapping));
                    } else {
                        missing.remove(it.getName());
                        EnumCodecSpecial.logger.debug(String.format("Registering codec for type '%s' with oid %d using Java enum type '%s'", it.getName(), it.getOid(), enumClass.getName()));
                        if (this.registrationPriority == EnumCodecSpecial.Builder.RegistrationPriority.LAST) {
                            registry.addLast(new EnumCodecSpecial(allocator, enumClass, it.getOid()));
                        } else {
                            registry.addFirst(new EnumCodecSpecial(allocator, enumClass, it.getOid()));
                        }

                    }
                }).doOnComplete(() -> {
                    if (!missing.isEmpty()) {
                        EnumCodecSpecial.logger.warn(String.format("Could not lookup enum types for: %s", missing));
                    }

                }).then();
            };
        }

        public static enum RegistrationPriority {
            FIRST,
            LAST;

            private RegistrationPriority() {
            }
        }
    }
}
