package net.dhleong.acl.test;

import java.util.List;

import junit.framework.TestCase;
import net.dhleong.acl.ArtemisPacketException;
import net.dhleong.acl.BadParseDetectingRunner;
import net.dhleong.acl.net.EnemyUpdatePacket;
import net.dhleong.acl.net.GenericMeshPacket;
import net.dhleong.acl.net.GenericUpdatePacket;
import net.dhleong.acl.net.OtherShipUpdatePacket;
import net.dhleong.acl.net.StationPacket;
import net.dhleong.acl.net.player.EngPlayerUpdatePacket;
import net.dhleong.acl.net.player.MainPlayerUpdatePacket;
import net.dhleong.acl.net.player.PlayerUpdatePacket;
import net.dhleong.acl.net.player.WeapPlayerUpdatePacket;
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
                /* // 1.661
                 * "09f5030000ff00c3b54547826e5242c6f84d472608c2b3d07f7f3ff40780bd020000000000000000000000",
                 */
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
                "04ed030000ffffffff7f040000005a003700360000000000803fcc02003f9a99993e6f12033b01000000d007000043980447000000003708994700000000ee51a0b79d9208409a99993e0000000000204200002042000020420000204201000009000000000000000200000000000000000000000000000000000000000000000000000000000000000000787c553f74773a3f1ab0263f9739183f946ffd3e00000000",
                "04ed030000801a0000005ac9574644e51c47b1f2473778b8a63f04ee030000841a00000018fcff3e39065a46db121d47a0ef4737b0bda63f04ef030000801a000000605f5c46c7121d4770004837c49aa63f04f0030000841a000000e803003f11c8b24748b3ac4748bbc7b7da4b22c004f1030000841a00000028f8ff3e33b79b4712dea947bcd5c737873533c004f2030000801a000000ea90aa47404820470ec0c8b6113f70bf00000000"
        };
        
        int[] shipsCreated = new int[] {
                /* from 1.661:
                2,
                2,
                1,
                2,
                2, */
                2,
                1,
                6
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
    
    public void testWhale() {
        String[] tests = new String[] {
            "10ea030000ff1f05000000580065006e006f000000000000000000000050c72d47be7f2bc376b324474220cb3c000000004011004000000000089ddc3e0000803f8ea2863f10eb030000ff1f05000000580065006e006f0000000000000000000000316a2d4711513b42cfdc2447f5ab11bc0000000098d6fd3f00000000a0d9d53d0000803fccf9a53f10ec030000ff1f05000000580065006e006f000000000000000000000062182e47f6e312c32e792547718e653d000000007a9f01400000000068ac873e0000803fad8f963f10ed030000ff1f05000000580065006e006f000000000000000000000044a02d47b6b4d1c1825b24478e382dbc0000000077c8fe3f00000000c06ec13d0000803fceeea63f00000000",
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            GenericUpdatePacket pkt = new GenericUpdatePacket(bytes);
            for (ArtemisPositionable o : pkt.mObjects) {
                try { 
                    BadParseDetectingRunner.testPositionable(o);
                } catch (RuntimeException e) {

                    System.out.println("EXCEPTION!");
                    pkt.debugPrint();
                    System.out.println("--> " + pkt);
                    throw e;
                }
            }
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            
//            assertCount(shipsCreated[i], pkt.mObjects);
            //assertEquals(shipsCreated[i], pkt.mObjects.size());
            
            System.out.println();
        }
    }
    
    public void testOther() {
        String[] tests = new String[]{
            "059f040000800830000038503c47d58847b7010105a0040000841a0000002600003f44df1f4707d38b470bed7fb5255db1bd00000000",
            "058c050000840a000000f401003ff75f64479124814727a847b7058d050000800a00000034b03447204c92478fa3c737058e050000841a00000018fcff3e0ccc2d47fe0b8e47c8c0473706e8f9bf00000000",
            "058c050000841a000000f601003fc9676447df2481475eb447b77380d2bf058d050000840a00000030f8ff3e4ca93447f44d92474b88c737058e050000801a000000e8dc2d47580f8e47bcc5473702e8f9bf00000000",
            "05ec050000848a0000000cfeff3ea085384780bd1f47d149c736c074534205ed050000800a000000dcd0dc4659884447fa86c73705ee050000841a0000001cfcff3e677a8347490f204734a2473760b785bf05ef050000800a00000009b0d74645349d47678cc73700000000",
            "0505080000840a000000f201003fdacc36479af9f046c88247b70506080000841a0000000300003fad0c0a4770562f47153788b3675b053d050708000084da000000c4bc5f3fe06f0647ebbe7747ee7133beac23d13f0100a08b6bc00508080000840a0000000900003fea8d794706b8aa46762a8ab400000000",
            "0508100000841a000000fb00003fe9909647faaec7464b9ec7b697456fbf0509100000801a000000c58d49478b57a44764a1c73770ea21c0050a100000800a000000ccc3b346268c8a479689c7b7050b100000801a300000f8807247908ed24677a647b6504cc0be0101050c100000801a0000007f8f6347290d97473aa3c7b7ae023b40050d100000841a0000000cfeff3ec533d446ddbc79461663c73682e30d3f00000000",
            "050e0d0000841a00000020fcff3ef24cee463b4e80470fb1473770baf13f050f0d0000841a00000008ffff3e93d64e47ead715478ec94736a8cdc8be05100d0000801a000000680488478a464d470f9fc8b7c01a2bc005110d0000801a000000390760470e605447f285c7b7fefc25c005120d0000040a300000e000003f326087474b0f97b6010105130d0000841a00000058c3f63e99c438471c6c1d47e377e73bf7b4f53f00000000",
            "058c050000ffffffff0705000000540072003200340000000000803ff201003f9a99993e6f12033b00000000dc050000e266644700000000d6248147000000008eb247b77280d2bf9a99993e010000002042000020420000204200002042010000000000000000000000000000000000000000000000000000000000000000000000e256a43e4482bb3ebeaf5e3f88f9103f90b1143f058d050000ffffffff0705000000540072003700390000000000803f30f8ff3e9a99993e6f12033b00000000dc05000018aa344700000000be4d924700000000d08bc737608b03409a99993e0000000020420000204200002042000020420100000000000000000000000000000000000000000000000000000000000000000000004a0f253f3a666a3fd0741b3f3bb9503fd790d13e058e050000ffffffff0705000000440065003100360000000000803f1cfcff3e3333333f6f12033b00000000dd050000f6da2d4700000000f50e8e470000000068c7473704e8f9bf3333333f01000000a0420000a0420000a0420000a042010000000000000000000000000000000000000000000000000000000000000000000000eef4293fedb0293f2b87153f9680b13e74a3393f00000000",
        };
        
        int[] shipsCreated = new int[] {
            
            2,
            3,
            3,
            4,
            4,
            6, 
            6,
            3,
            
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
                "01f8030000bc2af924000000003f9a99193f6f12833b0179007a440100000019184e4776c44a47db0f49400800000041007200740065006d006900730000000000a0420000a0420000a0420000a042005043480800000000",
        };
        

        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            PlayerUpdatePacket pkt = new MainPlayerUpdatePacket(bytes);
            
            ArtemisPlayer plr = pkt.getPlayer();
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            BadParseDetectingRunner.testPlayer(plr);
            
            System.out.println();
        }
    }
    
    public void testPlayerWeapons() {
        String[] tests = new String[] {
                "02ec03000020000010006a4100000000",
                "0279060000ef075408020604030012000200000069640003150015002002f70079002e0001f6ce00000000",
                "0220050000ffff7f070206040000000000000000000000000000000000000000000000000000010000000000000000000000000000",
                "0279060000ffff7f0802060400030012000200000069640003150015002002f70079002e00000000000000000100f600ce00000000"
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            PlayerUpdatePacket pkt = new WeapPlayerUpdatePacket(bytes);
            
            ArtemisPlayer plr = pkt.getPlayer();
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            BadParseDetectingRunner.testPlayer(plr);
            
            System.out.println();
        }
    }
    
    public void testPlayerEng() {
        String[] tests = new String[] {
            "0313040000ffffff000000000000000000000000000000000000000000000000000000000000000000abaaaa3e00000000abaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3eabaaaa3e000000000000000000000000",
        };
        
        for (int i=0; i<tests.length; i++) {
            byte[] bytes = hexStringToByteArray(tests[i]);
//            if (!BaseArtemisPacket.byteArrayToHexString(bytes).equals(s))
//                throw new RuntimeException("byte conversion fail");
            
            System.out.println();
            System.out.println("Test[" + i + "] of total " + tests.length);
            PlayerUpdatePacket pkt = new EngPlayerUpdatePacket(bytes);
            
            ArtemisPlayer plr = pkt.getPlayer();
            pkt.debugPrint();
            System.out.println("--> " + pkt);
            BadParseDetectingRunner.testPlayer(plr);
            
            System.out.println();
        }
    }
    public void testStation() {
        String[] tests = new String[] {
                /* //1.661
                "04eb030000020023d1384300000000",
                "04e8030000ff3f0400000044005300310000000000c8430000c84300000000e8030000bc7042470000000085df424700000000000000000000000000000000000000000004e9030000ff3f0400000044005300320000000000c8430000c84301000000e80300008b231f4700000000ee8b654700000000000000000000000000000000000000000004ea030000ff3f0400000044005300330000000000c8430000c84302000000e80300003279824700000000c4aa3b4700000000000000000000000000000000000000000004eb030000ff3f0400000044005300340000000000c8430000c84303000000e8030000347c7d47000000004067714700000000000000000000000000000000000000000000000000",
                "04c8070000ff1f0400000044005300310000000000c8430000c84300000000e8030000fd744247000000008fc84347000000000000000000000000000000000000000004c9070000ff3f0400000044005300320000000000c8430000c84301000000e8030000ff5b3e4700000000cade744700000000000000000000000000000000000000000004ca070000ff3f0400000044005300330000000000c8430000c84302000000e803000005470147000000007eb2454700000000000000000000000000000000000000000004cb070000a900040000004400530034000000030000006bdd034715ce1c4700000000",
                "04c60d0000ff1f0400000044005300310000000000c8430000c84300000000e8030000bfe2434700000000671a4447000000000000000000000000000000000000000004c70d0000ff3f0400000044005300320000000000c8430000c84301000000e8030000fb306b4700000000fd79254700000000000000000000000000000000000000000004c80d0000ff3f0400000044005300330000000000c8430000c84302000000e803000026346d4700000000d035104700000000000000000000000000000000000000000004c90d0000ab000400000044005300340000000000c84303000000b3546d47ec3e804700000000",
                "0460050000ff1f0400000044005300310000000000c8430000c84300000000e80300000710434700000000ad41444700000000000000000000000000000000000000000461050000ff1f0400000044005300320000000000c8430000c84301000000e8030000a88c544700000000f095144700000000000000000000000000000000000000000462050000ff1f0400000044005300330000000000c8430000c84302000000e80300008a5f6e4700000000ed6e754700000000000000000000000000000000000000000463050000ff1f0400000044005300340000000000c8430000c84303000000e80300004c56164700000000f2450847000000000000000000000000000000000000000000000000",
                */
                
                "0600040000ff3f0400000044005300310000000000c8430000c84300000000e803000086e4434700000000191944470000000000000000000000000000000000000601040000ff3f0400000044005300320000000000c8430000c84301000000e80300003f7b6c47000000006e575f470000000000000000000000000000000000000602040000ff3f0400000044005300330000000000c8430000c84302000000e803000018f66747000000003a540c470000000000000000000000000000000000000603040000ff3f0400000044005300340000000000c8430000c84303000000e80300000580f446000000003b43364700000000000000000000000000000000000000000000",
        };
        
        int[] created = new int[] {
                /*
                1,
                4,
                4,
                4,
                4,
                */
                
                4
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
