package net.dhleong.acl.protocol.core.helm;

import net.dhleong.acl.enums.ConnectionType;
import net.dhleong.acl.iface.PacketFactory;
import net.dhleong.acl.iface.PacketFactoryRegistry;
import net.dhleong.acl.iface.PacketReader;
import net.dhleong.acl.iface.PacketWriter;
import net.dhleong.acl.protocol.ArtemisPacket;
import net.dhleong.acl.protocol.ArtemisPacketException;
import net.dhleong.acl.protocol.BaseArtemisPacket;
import net.dhleong.acl.protocol.UnexpectedTypeException;

/**
 * Set climb/dive.
 */
public class HelmSetClimbDivePacket extends BaseArtemisPacket {
	private static final int TYPE = 0x0351A5AC;
    private static final byte SUBTYPE = 0x02;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return HelmSetClimbDivePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new HelmSetClimbDivePacket(reader);
			}
		});
	}

    private float mPitch;

    /**
     * @param pitch steering float in [-1, 1], where 0.0 is "centered" (neither
     * climbing nor diving, 1.0 is hard dive, -1.0 is hard climb
     */
    public HelmSetClimbDivePacket(float pitch) {
        super(ConnectionType.CLIENT, TYPE);

        if (pitch < -1 || pitch > 1) {
        	throw new IllegalArgumentException("Pitch out of range");
        }
        
        mPitch = pitch;
    }

    private HelmSetClimbDivePacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
    	int subtype = reader.readInt();

    	if (subtype != SUBTYPE) {
        	throw new UnexpectedTypeException(subtype, SUBTYPE);
    	}

    	mPitch = reader.readFloat();
    }

	public float getPitch() {
		return mPitch;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
    	writer.writeInt(SUBTYPE).writeFloat(mPitch);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mPitch);
	}
}
