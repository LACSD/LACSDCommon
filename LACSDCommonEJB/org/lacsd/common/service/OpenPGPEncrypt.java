package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c)   2011 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		OpenPGPEncrypt.java
//* Revision: 		version 1.0
//* Author:			asrirochanakul
//* Created On: 	Jul 8, 2011
//* Modified By:
//* Modified On:
//*
//* Description:	Encrypt files with Open PGP
//*
/*****************************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.util.io.Streams;

public class OpenPGPEncrypt {

private static OpenPGPEncrypt _INSTANCE = new OpenPGPEncrypt();

private OpenPGPEncrypt() {
	super();
}

public static OpenPGPEncrypt getInstance() {
	return _INSTANCE;
}

/**
 * Encrypt file
 * @param outputFileName
 * @param inputFileName
 * @param encKeyFileName
 * @param armor
 * @param withIntegrityCheck
 * @throws IOException
 * @throws NoSuchProviderException
 * @throws PGPException
 */
public void encryptFile(
        String          outputFileName,
        String          inputFileName,
        String          encKeyFileName,
        boolean         armor,
        boolean         withIntegrityCheck)
        throws IOException, NoSuchProviderException, PGPException
{
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFileName));
    PGPPublicKey encKey = PGPExampleUtil.readPublicKey(encKeyFileName);
    encryptFile(out, inputFileName, encKey, armor, withIntegrityCheck);
    out.close();
}

/**
 * Encrypt file
 * @param out
 * @param fileName
 * @param encKey
 * @param armor
 * @param withIntegrityCheck
 * @throws IOException
 * @throws NoSuchProviderException
 */
public void encryptFile(
    OutputStream    out,
    String          fileName,
    PGPPublicKey    encKey,
    boolean         armor,
    boolean         withIntegrityCheck)
    throws IOException, NoSuchProviderException
{
	/*
    if (armor)
    {
        out = new ArmoredOutputStream(out);
    }
    */

    try
    {    
        PGPEncryptedDataGenerator   cPk = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, withIntegrityCheck, new SecureRandom(), "BC");
            
        cPk.addMethod(encKey);
        
        OutputStream                cOut = cPk.open(out, new byte[1 << 16]);
        
        PGPCompressedDataGenerator  comData = new PGPCompressedDataGenerator(
                                                                PGPCompressedData.ZIP);
                                                                
        PGPUtil.writeFileToLiteralData(comData.open(cOut), PGPLiteralData.BINARY, new File(fileName), new byte[1 << 16]);
        
        comData.close();
        
        cOut.close();

        if (armor)
        {
            out.close();
        }
    }
    catch (PGPException e)
    {
        System.err.println(e);
        if (e.getUnderlyingException() != null)
        {
            e.getUnderlyingException().printStackTrace();
        }
    }
}

/**
 * Decrypt file
 * @param inputFileName
 * @param keyFileName
 * @param passwd
 * @param defaultFileName
 * @throws IOException
 * @throws NoSuchProviderException
 */
private void decryptFile(
        String inputFileName,
        String keyFileName,
        char[] passwd,
        String defaultFileName)
        throws IOException, NoSuchProviderException
{
    InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
    InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
    decryptFile(in, keyIn, passwd, defaultFileName);
    keyIn.close();
    in.close();
}

/**
 * decrypt the passed in message stream
 */
@SuppressWarnings("rawtypes")
private void decryptFile(
    InputStream in,
    InputStream keyIn,
    char[]      passwd,
    String      defaultFileName)
    throws IOException, NoSuchProviderException
{    
    in = PGPUtil.getDecoderStream(in);
    
    try
    {
        PGPObjectFactory        pgpF = new PGPObjectFactory(in);
        PGPEncryptedDataList    enc;

        Object                  o = pgpF.nextObject();
        //
        // the first object might be a PGP marker packet.
        //
        if (o instanceof PGPEncryptedDataList)
        {
            enc = (PGPEncryptedDataList)o;
        }
        else
        {
            enc = (PGPEncryptedDataList)pgpF.nextObject();
        }
        
        //
        // find the secret key
        //
        Iterator                    it = enc.getEncryptedDataObjects();
        PGPPrivateKey               sKey = null;
        PGPPublicKeyEncryptedData   pbe = null;
        PGPSecretKeyRingCollection  pgpSec = new PGPSecretKeyRingCollection(
            PGPUtil.getDecoderStream(keyIn));                                                                 
        
        while (sKey == null && it.hasNext())
        {
            pbe = (PGPPublicKeyEncryptedData)it.next();
            
            sKey = PGPExampleUtil.findSecretKey(pgpSec, pbe.getKeyID(), passwd);
        }
        
        if (sKey == null)
        {
            throw new IllegalArgumentException("secret key for message not found.");
        }
        
        InputStream         clear = pbe.getDataStream(sKey, "BC");
        
        PGPObjectFactory    plainFact = new PGPObjectFactory(clear);
        
        PGPCompressedData   cData = (PGPCompressedData)plainFact.nextObject();

        InputStream         compressedStream = new BufferedInputStream(cData.getDataStream());
        PGPObjectFactory    pgpFact = new PGPObjectFactory(compressedStream);
        
        Object              message = pgpFact.nextObject();
        
        if (message instanceof PGPLiteralData)
        {
            PGPLiteralData ld = (PGPLiteralData)message;

            String outFileName = ld.getFileName();
            if (outFileName.length() == 0)
            {
                outFileName = defaultFileName;
            }

            InputStream unc = ld.getInputStream();
            OutputStream fOut =  new BufferedOutputStream(new FileOutputStream(outFileName));

            Streams.pipeAll(unc, fOut);

            fOut.close();
        }
        else if (message instanceof PGPOnePassSignatureList)
        {
            throw new PGPException("encrypted message contains a signed message - not literal data.");
        }
        else
        {
            throw new PGPException("message is not a simple encrypted file - type unknown.");
        }

        if (pbe.isIntegrityProtected())
        {
            if (!pbe.verify())
            {
                System.err.println("message failed integrity check");
            }
            else
            {
                System.err.println("message integrity check passed");
            }
        }
        else
        {
            System.err.println("no message integrity check");
        }
    }
    catch (PGPException e)
    {
        System.err.println(e);
        if (e.getUnderlyingException() != null)
        {
            e.getUnderlyingException().printStackTrace();
        }
    }
}


public static void main(String[] args) throws Exception {

    Security.addProvider(new BouncyCastleProvider());
    
    OpenPGPEncrypt e = OpenPGPEncrypt.getInstance();

    String output = "c:/openpgp/20110728125233_000_10006.pgp";
    String input = "c:/openpgp/input/20110728125233_000_10006.xml";
//    String calPersPublicKey = "c:/openpgp/CalPERS_PSR10006.asc";
    String publicKey = "c:/openpgp/LACSD_pub.asc";
    String secretKey = "c:/openpgp/LACSD_secret.asc";
    String password = "welcome";
    
    e.encryptFile(output, input, publicKey, false, false);

    e.decryptFile(output, secretKey, password.toCharArray(), null);
}

}