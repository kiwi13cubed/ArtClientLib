package net.dhleong.acl.test;

import java.util.List;

import junit.framework.TestCase;
import net.dhleong.acl.ArtemisPacketException;
import net.dhleong.acl.BadParseDetectingRunner;
import net.dhleong.acl.net.EnemyUpdatePacket;
import net.dhleong.acl.net.GenericMeshPacket;
import net.dhleong.acl.net.GenericUpdatePacket;
import net.dhleong.acl.net.OtherShipUpdatePacket;
import net.dhleong.acl.net.PlayerUpdatePacket;
import net.dhleong.acl.net.StationPacket;
import net.dhleong.acl.world.ArtemisPlayer;
import net.dhleong.acl.world.ArtemisPositionable;
import net.dhleong.acl.world.BaseArtemisShip;

public class ObjectParsingTests extends TestCase {
    
    public void testMesh() {
        
        String[] tests = new String[] {
                "0ce9030000bfff7f0300f5364700000000001449470000000000000000000000000000000000000000000000000000000000000000070000004f0042004a004500430054000000160000006400610074005c0061007200740065006d00690073002d00730063006f00750074002e006400780073000000180000006400610074005c0061007200740065006d00690073005f0064006900660066007500730065002e0070006e00670000000000484317cdcccc3d0000803f000000000000803f00006144000080bf0200000020000000020000002000000000000000",
                "0ce9030000ffffff0300f536470000000000144947000000000000000000000000000000000000000000000000000000000000000000000000070000004f0042004a004500430054000000160000006400610074005c0061007200740065006d00690073002d00730063006f00750074002e006400780073000000180000006400610074005c0061007200740065006d00690073005f0064006900660066007500730065002e0070006e00670000000000484300cdcccc3d0000803f000000000000803f00006144000080bf7402000000200000000200000020000000",
                "0ce90300009ff7fe0300f536470000000000144947000000000000000000000000000000000000000000000000070000004f0042004a004500430054000000160000006400610074005c0061007200740065006d00690073002d00730063006f00750074002e006400780073000000180000006400610074005c0061007200740065006d00690073005f0064006900660066007500730065002e0070006e006700000000004843cdcccc3d0000803f000000000000803f00006144000080bf74020000002000000002000000200000000cea030000ffffff0380d18947000000000048f746000000000000000000000000000000000000000000000000000000000000000000000000050000004f0042004a0032000000160000006400610074005c0061007200740065006d00690073002d00730063006f00750074002e006400780073000000180000006400610074005c0061007200740065006d00690073005f0064006900660066007500730065002e0070006e006700000000004843e2cdcccc3d0000803f000000009a99993e00006144000080bf740200000020000000020000002000000000000000",
        };
        
        int[] counts = new int[] {
                1,
                1,
                2
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            GenericMeshPacket pkt = new GenericMeshPacket(bytes);
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            for (ArtemisPositionable o : pkt.mObjects) {
                BadParseDetectingRunner.testPositionable(o);
                assertNotNull("Obj name", o.getName());
            }
            assertCount(counts[i], pkt.mObjects);
            System.out.println();
        }
    }
    
    public void testGenerics() { 
        
        String[] tests = new String[] {
                "09f5030000ff00c3b54547826e5242c6f84d472608c2b3d07f7f3ff40780bd020000000000000000000000",
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            GenericUpdatePacket pkt = new GenericUpdatePacket(bytes);
            for (ArtemisPositionable o : pkt.mObjects) {
                BadParseDetectingRunner.testPositionable(o);
            }
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            System.out.println();
        }
    }
    
    public void testEnemy() {
        String[] tests = new String[] {
                /* From 1.661
                "027c070000dd3a6f007c04000000460037003100000000000000cdcc4c3f6f12833b89130000a94ec3473250c3474215813ed0cc4cbed0cccc3e000096430000964300001643000016433518000000d27f023fe4353f3f16bb0a3f4f7fda3e1314d63e027d070000dd3a6f007c04000000500037003400000000000000cdcc4c3f6f12833b89130000a94ec347b2f89f414215813ed0cc4cbed0cccc3e000096430000964300001643000016433623000000e2f5f03e387ce83e6b121c3fc68b623ffe60e53e00000000",
                "02470c0000853a40027c040000005900330032000000000000002d0c9247c3c1ba464215813ed0cc4cbed0cccc3e22000000002821fa3e2cee623fc6ad2f3fb3ac0c3fe9fbc03e02480c0000ffffffff7f040000004f003100360000000000803f00000000cdcc4c3f6f12833b010000008a1300003df02b470000000001189f47000000004215813ed0cc4cbed0cccc3e0000000000af430000af430000964300009643010043100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000b0f5d73e2607133f1e09423f875ff63ebfba453f00000000",
                "0262080000ff3ffffd7f0400000047003900350000000000803f000000009a99993e6f12033b01000000d0070000b2606043000000006afb9c47000000004215013ed0ccccbd9a99993e0000204200002042000020420000204201000443000000000000000000000000000000000000000000000000000000000000000000000000000000000000001d7f0e3f4265543f8bde2b3f506ac13e2011433f00000000",
                "02b4050000fb3a5f007c040000005a003800320000000000803f9a99993e6f12033b01000000d0070000654fc347a33c53474215013ed0ccccbd9a99993e00002042000020420000204200002042010021000000b6b4743f5f4cfc3e0173003fd045353fcf0f013f02b5050000ffffffff7f0400000056003000370000000000803f000000009a99993e6f12033b01000000d0070000654fc3470000000067a75447000000004215013ed0ccccbd9a99993e000000000020420000204200002042000020420100002600000000000000000000000000000000000000000000000000000000000000000000000000000000000000001714253f858dc23ea4d06b3f32e4323f0cbb053f00000000",
                "0268050000851a60007c04000000580034003700000000000000193dcb462f4402464215013ed0ccccbd0d14000000709a1e3f6252173f31dab13e851a293fd880d23e0269050000ffffffff7f040000005a003000350000000000803f000000009a99993e6f12033b01000000d2070000b4d4cc460000000017440246000000004215013ed0ccccbd9a99993e0000000000f0420000f0420000f0420000f04201000d740000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a212373f9a8b4d3fd6e81d3fb5ec0d3ff29a5f3f00000000"
                */
                "0475040000fb3a5f007c0400000049003400340000000000803f9a99993e6f12033b01000000d107000050896d472650c3474215013ed0ccccbd9a99993e0000a0420000a0420000a0420000a042010030000000a8a9203ff0bbc43e6c5b363fe644263fe234243f0476040000fb3a5f007c0400000058003100350000000000803f9a99993e6f12033b01000000d0070000501f6e472650c3474215013ed0ccccbd9a99993e000020420000204200002042000020420100060000005c18943e04d01b3f9d539b3e4495553ffacb163f00000000",
        };
        
        int[] shipsCreated = new int[] {
                /* from 1.661:
                2,
                2,
                1,
                2,
                2, */
                2
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            EnemyUpdatePacket pkt = new EnemyUpdatePacket(bytes);
            for (ArtemisPositionable o : pkt.mObjects) {
                BadParseDetectingRunner.testShip((BaseArtemisShip) o);
            }
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            
//            assertCount(shipsCreated[i], pkt.mObjects);
            assertEquals(shipsCreated[i], pkt.mObjects.size());
            
            System.out.println();
        }
    }
    
    public void testOther() {
        String[] tests = new String[]{
                
                "03040d0000ffffffff0705000000440065003900350000000000803f0efeff3e3333333f6f12033b00000000dd050000bcc7634700000000abd39f46000000001cafc73658a365bf3333333f00000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000c05c133f6f3db73e781b3c3fc834173f5b08473f03050d0000ffffffff0705000000440065003400300000000000803fa0feff3e3333333f6f12033b00000000dd05000028ef5b47000000006760824700000000b3df9336aa5821c03333333f01000000a0420000a0420000a0420000a04201000000000000000000000000000000000000000000000000000000000000000000000000000066a5663f3287193f0659363fb0a0be3e4a03253f03060d0000ffffffff0705000000540072003300320000000000803f8800003f9a99993e6f12033b00000000dc050000174ecd46000000007508a04700000000cbc155b61da948409a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000000000008742dd3e1094213f41a1533f0b91eb3e78d9083f03070d0000ffffffff0705000000440065003000380000000000803ff8fcff3e3333333f6f12033b00000000dd0500002364ed4600000000d28f664700000000c6f919373e8f1a403333333f00004eb62e410000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000c5d2fb3e6a81013fd2d7023f34dc333f57a7de3e03080d0000ffffffff0705000000540072003600330000000000803ff201003f9a99993e6f12033b00000000dc050000ae1a974700000000607791470000000070a747b776b0f1bf9a99993e010000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000000000d55e9d3ed7a81e3f28b02d3f88622a3f6072163f00000000",
                "0325080000ffffffff0705000000540072003900390000000000803f0300003f9a99993e6f12033b00000000dc0500000d05124700000000ea87d246000000001be214b04463fc3d9a99993e010000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000000000c8bd303ff4f5f93ed06b013ffca14a3f6aa5013f0326080000ffffffff0705000000440065003600320000000000803f0000003f3333333f6f12033b00000000dd050000aafd4b4700000000ceac9a47000000000000000020223dc0d0cccc3e00000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000a90e3b3f18c8253f92bd153f5f30493fce05e73e0327080000ffffffff0705000000540072003500370000000000803ff8ffff3e9a99993e6f12033b00000000dc050000587d944700000000e038a24700000000d5be6f32f3edf2bf9a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000000000000f0aa13e201a5d3f3c48843e5c25613f52fb283f0328080000ffffffff0705000000440065003700340000000000803f0000003f3333333f6f12033b00000000dd050000ea6ea3470000000091bda847000000000000000076462dc0d0cccc3e00000000a0420000a0420000a0420000a0420100000000000000000000000000000000000000000000000000000000000000000000000000008227413f33d1ff3ee45b0b3fc790163f40e0393f0329080000ffffffff0705000000540072003700390000000000803ff8ffff3e9a99993e6f12033b00000000dc0500009de48d47000000000232874700000000fb90bf32c097ddbf9a99993e000000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000000000995d193f4895573ff27f123f7488533fea38db3e032a080000ffffffff0705000000440065003500310000000000803f0000003f3333333f6f12033b00000000dd050000e492014700000000f5dd024700000000c3838c31febd673fd0cccc3e00000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000bb5baa3e00e0193f2ab02e3f034c1b3f76d0543f00000000",
                "03be050000800a0000007679fc466160bf4625b347b703bf050000800a000000a6336c47810b45472fab473703c0050000844a00000018fcff3e9c1bfe466452204717ab4737010003c1050000801a000000ac25a44745f7724777b2c7b710bc3ec003c2050000840a00000030f8ff3eb640324720335b47e89cc73700000000",
                "03be050000841a000000f201003f5fba0f4769e5d1460aa247b7d6f28a3f03bf05000080aa00000011568a471053424717a61937588d173f17f2ecc003c0050000840a00000018fcff3e8f65a6469e6e1247b8b2473703c1050000841a000000e403003fce8ba0473c70454720b1c7b7a0b63ec003c2050000800a000000e7ae38473f6c4847d290c73700000000",
                "033e0e0000800a0000008b401e4703b2bd4682fa9134033f0e0000800a000000fd44ae462cc73e475e9007b503400e0000800a000000aad63e47040daa47080fa0b503410e0000848a8030003000003fc43a8d4723040c479fc482b5000080c1d74f7d3f122f7d3f9c707d3f03420e0000840a0000001500003fc5559b46e6eee34641f3eab400000000",
                "033e0e0000841a0000001811003fb3e4364749c1f6468eb3d4b857a13e3f033f0e0000800a000000bde2f0462c8d2f471790c7b703420e0000848a803800fa00003fc53dd846636d0647397ac7b6000000c176be7f3f0000803fb19d7f3f3bdf7f3f00000000",
                "03a2070000801a000000528e6347ce69c14699cbc6b5a5c004be03a3070000841a0000003f00003f4bbe63473756064721b8c7b5b7f7163e03a4070000801a000000cc5dfb46b96c27471c654736b0f7ac3e03a5070000800a0000006d34864700c88247514c473703a6070000840a42000018fcff3e20d55247d4170c47aa894737f07c7fc1275c7f3f03a7070000840a00000030f8ff3ecafc804735dc8c477daac73700000000",
                "0300060000801a000000259e2a47d29a7147d9adc7375e6910400301060000849a001100f900003f22714b47266334473c72c7b6ea68003f9eef7fc19c707d3f61917d3f00000000",
                "039e050000801a000000e5b6c146b1799347bec647373a50d53f039f05000086bac03f0000000000f201003ff12a8f470eef53473db623b75c08a3bf65916d3e623bffc061917d3f75147e3fb0f37d3f9c707d3febd27d3f26b27d3f3a357e3fff557e3f03a0050000841a000000e403003fb7b76047fcb644479d9fc7b71cba1dc003a1050000800a002800df1ee1469ff958461ca247b73220503f3220503f00000000",
                "03e80a0000840a0000002cfdff3ea1d5c54644f98b47a9740c3703e90a0000840a10000080f9ff3ed599f34634ae8e478f21a13753b87e3f03ea0a0000801a000000e87da647e7a49c47aec447b7909cdbbf03eb0a0000840a000000f8ffff3e205bed46e8f6aa475f7fb43203ec0a0000840a000000f401003f9efc964717e605478ea847b703ed0a0000840a0000001b00003f12833d4628cd9b46c34a22b500000000",
                "03a1060000ffffffff0705000000540072003700340000000000803f30f8ff3e9a99993e6f12033b00000000dc050000e8c03e4700000000e14ea447000000002bb4c73764dc41c09a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000000000007a473d3fcb85323f2856603f8bd7f83e73e21f3f03a3060000ffffffff0705000000440065003000310000000000803f18fcff3e3333333f6f12033b00000000dd0500002c817a4700000000017339470000000067ae4737e053afbf3333333f00000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000f0202b3f58b2c53e8819113f8cac5f3f3fa31f3f03a4060000ffffffff0705000000440065003200330000000000803f7d00003f3333333f6f12033b00000000dd05000026bb84470000000052b606470000000035db47b68837ffbe3333333f00000000a0420000a0420000a0420000a04201000000000000000000000000000000000000000000000000000000000000000000000000000012513c3fbada433f8050a63e33f7cc3ee8d5f33e00000000",
                "03c50c0000ffffffff0705000000540072003600390000000000803f0000003f9a99993e6f12033b00000000dc050000ce8d3c47000000009bc91047000000004af09532869e043f9a99993e010000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000000000329d4c3f057a4f3fbdd0113f27b7c63e22cf103f03c60c0000ffffffff0705000000540072003700350000000000803ffaffff3e9a99993e6f12033b00000000dc050000d6a6ae47000000004ab1bf4600000000a7082f2f60865dbf9a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000000000002985943e7a89093ff1a82b3fa33b9e3e48783d3f03c70c0000ffffffff0705000000540072003400300000000000803ff8ffff3e9a99993e6f12033b00000000dc05000026ed5546000000007e74a447000000001ca84632b86530409a99993e000000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000000000f6c9943e483cf03ef595473f1efb0e3fbe14123f03c80c0000ffffffff0705000000440065003900380000000000803f0000003f3333333f6f12033b00000000dd050000f4006b470000000045bca5460000000000000000fe9f213ed0cccc3e00000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000000000d818d23e169e573f2421453f7ea50b3f8dc7f93e03c90c0000ffffffff0705000000540072003800320000000000803ff8ffff3e9a99993e6f12033b00000000dc050000d9eaaa47000000001fe37147000000004123713230d110c09a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000000000008d122d3fc9d5e43ed109353f9ecd1b3fc1aafa3e03ca0c0000dfb63ffc0705000000440065003700310000000000803f0000003f3333333f6f12033bdd0500007ce579470013be4600000000412c85bfd0cccc3e0000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000010b4213f75b96d3f19a93f3f0859373f91bae13e00000000",
                
                "034a0a000000ea020000357190467b6557b39699993e0100c24ca042c24ca042034b0a0000843a000000f83dda3e1ab308474b4a1c472ab1873c9d81e5be9699993e034c0a0000841a0000008419003f12db394697749c4756cf84b879a2b2bd00000000",
        };
        
        int[] shipsCreated = new int[] {
        
                5,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                4,
                0,
                3,
                6,
                
                3
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            OtherShipUpdatePacket pkt = new OtherShipUpdatePacket(bytes);
            for (ArtemisPositionable o : pkt.mObjects) {
                try {
                    BadParseDetectingRunner.testShip((BaseArtemisShip) o);
                } catch (RuntimeException e) {

                    System.out.println("EXCEPTION!");
                    pkt.debugPrint();
                    System.out.println("--> " + pkt);
                    throw e;
                }
            }
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            
            
            assertCount(shipsCreated[i], pkt.mObjects);  
            
            System.out.println();
        }
    }
    
    public void testPlayer() throws ArtemisPacketException {
        
        /* From 1.661 
        String[] tests = new String[] {
                "011004000084280d00000000c00ffc000000003f79007a4483974a47d7874947db0f494021af0800000041007200740065006d00690073000000fd7532251ed7ee027a4b153a24dba432ff3c92eb332d5e985d75777c7c9e00000000",
                "01ec030000800000000000000000000080f3794400000000",
                "015c04000000000040000000000000005d04000000000000",
                "01e803000080000002000000000000004e007a440100000000",
                "015c0400000000400000000080000000bb6bfa41a8ffb93c00000000",
                "01f803000080000000000000008200003d007a44000000000100000000",
                "01e40700008100400000000000000000e907000083ad4e443e3c474300000000",
                "01ec030000c02002000000000000000000dc7163447c5b2b474fe12a4100000000",
                "01ec030000c020420000000000000000027aca7744fa9c3447f1d2cd3f4ade864200000000",
                "01ec030000c2200200000000000000000000803f01d8dc6d44ce0e49474160e53d00000000",
                "018e07000080a8004094000000000000028f6644c5a59147b13c4547fa04adb801000000a8fecb3c54ac683de0f88c3d00000000",
                "01e803000000100000fe010000000000114e1a0c17506f3f89236c3f33f44e3f0dee4d3fe7e74c3f7ae10b3f95296d3f5476313f00000000",
                "018e07000080a80040940000c0000000955a7844b7e18e4767bb4d473d7ff7b9d30800003fe5473b277eba3b6b50ff3bb8080741b808074100000000",
                "013906000080100000be050000000000f33077444ba2ab2c3ac6373cde2d903cf9d4143e67a4ff3e1da51e3f84d6d83dcdaa263f0000803f00000000",
                "01e8030000800000007ee07000000000b3f4ed420cc7ba3c74029a3cb57b723c7bf2303c14b7d13b6e12033b0000803f0000803f0000803f00000000000000",
                "01e803000080000000fe81c101000000a79bc43d0f72253f9de7263f2b5d283fcdf4293f8e4b2b3f67892c3f873e2e3fabb82f3f000000000000000008080800000000",
                "01ec03000082a80301940100000000008fc2f53da20f7a449e336e4721ad1047848dad8eb43a1f3f0ad7233d0000000068f1223cffcd973cdf6c683c5eb0ba3c00000000",
                "0194070000bf2af92400fe01fe0f800000000000000000000000003f9a99193f6f12833b0120d1794401000000a0413a47523f3847db0f49400800000041007200740065006d006900730000000000a0420000a0420000a0420000a0420050434808abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e080206040dffffffff3d010804010000008c5a990338000c04e06fe3037000000000",
                "01ec030000bffeffa5bfffcfbefffe0000000000000000000000003f9a99193f6f12833b0179007a440100000000000000d46e3a4700000000fec849470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a042000000000050434808000000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e0000000000080206040d98010c05620400000000000050be0a040000000000000000000000a600fbbe00000000",
                "015e0a0000bf7fffedf2ffedfeeff70000000000000000000000003f9a99193f6f12833b0109007a4400000100000000000000005043471ab6f5400050434700000000b0ad7ebf000000007e420800000041007200740065006d006900730000000000a0420000a0420000a0420000a042000000000050434800080000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e0000000000080206040d074b8644c4c301c26b6b5e4334121a42dca89e42c73339c400000000006c4909d72300000000",
                "01ec0300009ffffff7fffffbfe2f2f0000000000000000000000003f9a99193f6f12833b79007a44000001000000000000008c5649470000000036ce3a470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a04200000000000050434800080000000000000000000000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e000000000000080206040d3c2f730333010c040600000065780078ffffffff2f000c040000002f006500000000",
                "01e8030000ffffffffffffffffffff0000000000000000000000003f9a99193f6f12833b010013007a44000001000000000000000050434700000000005043470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a0420000000001005043480000080000000000000000000000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e0000000000000000080206040d2800080429000c049005990335343232000000000000000000000000000000000233009800000000",
                "01e8030000ffffffffffffffffffff0000000000000000000000003f9a99193f6f12833b010040007a44000001000000000000000050434700000000005043470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a0420000000000005043480000080000000000000000000000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e0000000000000000080206040d2800080429000c049005990335343232000000000000000000000000000000000233009800000000",
                "01e4070000ffffffffffffffffffff0000000000000000000000003f0000003fa69b443b010075007a4400000100000004000000ce214d4700000000ef4a48470000000000000000db0f49400000000000000c000000550053005300200041007700650073006f006d0065000000000048430000484300004843000048430000000000005043480000080000000000000000010000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e00000000000000000a0307056900000000000000004e1452410000000000000000306f9a0301000200000001000200000000000000",
                "01f8030000ffffffffffffffffffff0000000000000000000000003f9a99193f6f12833b010050007a4400000100000003000000bc7d4a47000000007bc744470000000000000000db0f49400000000000000c000000550053005300200041007700650073006f006d00650000000000dc420000dc420000a0420000a0420000000000005043480000080000000000000000010000000000000000000000000000000000000000000000000000000000000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e000000000000000014070d0b03000000000000000000000000000000000d010c040600000001000101000001030203000000000000",
        };
        */
        
        String[] tests = new String[] {
                "01ec030000ffffffff0f00000000000000000000003f9a99193f6f12833b010079007a4400000100000000000000495439470000000001c54e470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a04200000000000050434800000800000000000000000000000000000000000000000000",
                "01ec030000ffffffff0f00000000000000000000003f9a99193f6f12833b010020a819440000010000000000000096fe3e4700000000d4524a470000000000000000db0f49400000000000000800000041007200740065006d006900730000000000a0420000a0420000a0420000a04200000000000050434800000800000000000000000000000000000000000000000000",
                "01f8030000bc2af924000000003f9a99193f6f12833b0179007a440100000019184e4776c44a47db0f49400800000041007200740065006d006900730000000000a0420000a0420000a0420000a042005043480800000000"
        };
        

        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            PlayerUpdatePacket pkt = new PlayerUpdatePacket(bytes);
            
            ArtemisPlayer plr = pkt.getPlayer();
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            BadParseDetectingRunner.testPlayer(plr);
            
            System.out.println();
        }
    }
    
    public void testStation() {
        String[] tests = new String[] {
                "04eb030000020023d1384300000000",
                "04e8030000ff3f0400000044005300310000000000c8430000c84300000000e8030000bc7042470000000085df424700000000000000000000000000000000000000000004e9030000ff3f0400000044005300320000000000c8430000c84301000000e80300008b231f4700000000ee8b654700000000000000000000000000000000000000000004ea030000ff3f0400000044005300330000000000c8430000c84302000000e80300003279824700000000c4aa3b4700000000000000000000000000000000000000000004eb030000ff3f0400000044005300340000000000c8430000c84303000000e8030000347c7d47000000004067714700000000000000000000000000000000000000000000000000",
                "04c8070000ff1f0400000044005300310000000000c8430000c84300000000e8030000fd744247000000008fc84347000000000000000000000000000000000000000004c9070000ff3f0400000044005300320000000000c8430000c84301000000e8030000ff5b3e4700000000cade744700000000000000000000000000000000000000000004ca070000ff3f0400000044005300330000000000c8430000c84302000000e803000005470147000000007eb2454700000000000000000000000000000000000000000004cb070000a900040000004400530034000000030000006bdd034715ce1c4700000000",
                "04c60d0000ff1f0400000044005300310000000000c8430000c84300000000e8030000bfe2434700000000671a4447000000000000000000000000000000000000000004c70d0000ff3f0400000044005300320000000000c8430000c84301000000e8030000fb306b4700000000fd79254700000000000000000000000000000000000000000004c80d0000ff3f0400000044005300330000000000c8430000c84302000000e803000026346d4700000000d035104700000000000000000000000000000000000000000004c90d0000ab000400000044005300340000000000c84303000000b3546d47ec3e804700000000",
                "0460050000ff1f0400000044005300310000000000c8430000c84300000000e80300000710434700000000ad41444700000000000000000000000000000000000000000461050000ff1f0400000044005300320000000000c8430000c84301000000e8030000a88c544700000000f095144700000000000000000000000000000000000000000462050000ff1f0400000044005300330000000000c8430000c84302000000e80300008a5f6e4700000000ed6e754700000000000000000000000000000000000000000463050000ff1f0400000044005300340000000000c8430000c84303000000e80300004c56164700000000f2450847000000000000000000000000000000000000000000000000",
        };
        
        int[] created = new int[] {
                1,
                4,
                4,
                4,
                4,
        };
        
        for (int i=0; i<tests.length; i++) {
            
            byte[] bytes = hexStringToByteArray(tests[i]);
            System.out.println("Test[" + i + "] of total " + tests.length);
            System.out.println("--> " + tests[i]);
            
            StationPacket pkt = new StationPacket(bytes);
            for (ArtemisPositionable p : pkt.getCreatedObjects()) {
                BadParseDetectingRunner.testPositionable(p);
            }
            pkt.debugPrint();
            assertCount(created[i], pkt.getCreatedObjects());
        }
    }

    private static void assertCount(int expected, List<ArtemisPositionable> mObjects) {
        if (mObjects.size() < expected) {
            fail("Must be at least " + 
                    expected +" objs; only created: " + mObjects.size());
        }
    }

    /** from stack overflow */
    public static byte[] hexStringToByteArray(String s) {
        s = s.toUpperCase(); // yes?
        
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
