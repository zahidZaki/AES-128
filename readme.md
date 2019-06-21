Advanced Encryption Standard
=================================================================
This is a Java reimplementation of the Advanced Encryption Standard.

The original article can be found here: https://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.197.pdf

The reference implementation can be found in internet. different with most implementation, here i implement Eculicd alogrithm and other mathmatical details, just
see Class S_box for details.

How to use?
----------
* If you only want to verify the correctness of the alogrithm, you can simply run Main function. In the main there are methods provide AES-128 encryption and decryption process wthin a state and provide OFB pattern to encrypt long text and decrypt encryptions. annotate the corresponding methods to see other methods effect.
* If you want to import to your project as a tool. please use follow methods, details
in doc. in OFB class
  ```
  public String encryptstringtobytes(String plaintext, String inputkey, String IV)
  ```
  ```
  public String decryptparseString(String encryption, String inputkey, String IV)
  ```

Example
------
input keys:`000102030405060708090a0b0c0d0e0f`
plaintext:`00112233445566778899aabbccddeeff`
output:`69C4E0D86A7B0430D8CDB78070B4C55A`
intermediate result can be found in [result](./result.txt) including OFB example.
if you want to know intermediate result of OFB pattern, just replace function `encryptstatenoprint(byte[][] state)` by `encryptstate(byte[][] state)` in functions  `encryptstringtobytes(String plaintext, String inputkey, String IV)` and `decryptparseString(String encryption, String inputkey, String IV)`. then rerun main function, it will print all details.
