package ngo.nabarun.test.ngo_nabarun_test.configs;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class FlexibleIdCodec implements Codec<String> {

    @Override
    public String decode(BsonReader reader, DecoderContext decoderContext) {
        BsonType currentType = reader.getCurrentBsonType();
        if (currentType == BsonType.OBJECT_ID) {
            return reader.readObjectId().toHexString();
        } else if (currentType == BsonType.STRING) {
            return reader.readString();
        } else {
            throw new BsonInvalidOperationException("Unsupported ID type: " + currentType);
        }
    }

    @Override
    public void encode(BsonWriter writer, String value, EncoderContext encoderContext) {
        writer.writeString(value); // Always store as string
    }

    @Override
    public Class<String> getEncoderClass() {
        return String.class;
    }
}
