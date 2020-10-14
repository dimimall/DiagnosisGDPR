package com.weblogical.dimitra.diagnosisgdpr;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    WebView webview1,webview2;
    String html1,html2,html_en1,html_en2;
    private SignaturePad mSignaturePad;
    EditText name,birthdate,adt,phone,fax,asfalia,address,tk,city,email,datetime;
    TextView text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17;
    Button save,sendemail,viewpdf,clear;
    File myFile;
    Bitmap signatureBitmap;
    Calendar birthCalendar = Calendar.getInstance();
    Calendar dateCalendar = Calendar.getInstance();
    int flag=0;
    public static final String FONT = "assets/LiberationSans-Regular.ttf";
    void setLanguage(String lc)
    {
        Locale locale = new Locale(lc);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseFont fonty = null;
        try {
            fonty = BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fonty.setSubset(true);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        webview1 = (WebView)findViewById(R.id.webView);
        webview2 = (WebView) findViewById(R.id.webView2);

        name = (EditText)findViewById(R.id.edittext1);
        birthdate = (EditText)findViewById(R.id.edittext2);
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, birth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        adt = (EditText)findViewById(R.id.edittext3);
        phone = (EditText)findViewById(R.id.edittext4);
        fax = (EditText)findViewById(R.id.edittext10);
        asfalia = (EditText)findViewById(R.id.edittext11);
        address = (EditText)findViewById(R.id.edittext5);
        tk = (EditText)findViewById(R.id.edittext6);
        city = (EditText)findViewById(R.id.edittext7);
        email = (EditText)findViewById(R.id.edittext8);
        datetime = (EditText)findViewById(R.id.edittext9);
        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, currentdate, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        text1 = (TextView)findViewById(R.id.textView);
        text2 = (TextView)findViewById(R.id.textView2);
        text3 = (TextView)findViewById(R.id.textView3);
        text4 = (TextView)findViewById(R.id.textView4);
        text5 = (TextView)findViewById(R.id.textView5);
        text6 = (TextView) findViewById(R.id.textView6);
        text7 = (TextView)findViewById(R.id.textView7);
        text8 = (TextView)findViewById(R.id.textView8);
        text9 = (TextView)findViewById(R.id.textView9);
        text10 = (TextView)findViewById(R.id.textView10);
        text11 = (TextView)findViewById(R.id.textView11);
        text12 = (TextView)findViewById(R.id.textView12);
        text13 = (TextView)findViewById(R.id.textView13);
        text14 = (TextView)findViewById(R.id.textView14);
        text15 = (TextView)findViewById(R.id.textView15);
        text16 = (TextView)findViewById(R.id.textView16);
        text17 = (TextView)findViewById(R.id.textView17);

        clear = (Button)findViewById(R.id.button3);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText(null);
                birthdate.setText(null);
                adt.setText(null);
                phone.setText(null);
                fax.setText(null);
                asfalia.setText(null);
                address.setText(null);
                tk.setText(null);
                city.setText(null);
                email.setText(null);
                datetime.setText(null);
            }
        });

        save = (Button)findViewById(R.id.save);
        save.setEnabled(true);
        viewpdf = (Button)findViewById(R.id.button2);
        viewpdf.setEnabled(false);
        sendemail = (Button)findViewById(R.id.button);

        html_en1 = "<html><body style=\"font-family:arial\"><ul><li><b>Purpose of data processing1.</b>The purpose S.Y.Diagnosis Laboratory Center Limited (Diagnosis) collects, processes and maintains personal data, is due to our activities in relation to the laboratory tests concerning you.</li>" +
                "<li><b>Type of personal data that they are or will be subjected to processing</b><ul><li>Name and Surname</li>" +
                "<li>Date of Birth</li>"+
                "<li>Identity and/or passport number</li>" +
                "<li>Contact details such as address, phone, fax and e-mail.</li></br>" +
                "<li>Information relating to your previous transactions with the Lab</li>" +
                "<li>Current treatments</li>" +
                "<li>Test results</li></ul>" +
                "</li><li><b>Update / Saved data</b></li>" +
                "<ul><li>Our records will be periodically updated in the same way as which information have been collected</li><li>Your personal data is stored in the secure files in printed and electronic form and kept on our facilities.  Only our staff has access to the files. We have security measures installed to ensure the data confidentially and these security measures are subject to constant review and upgrade.</li></ul></li>" +
                "<li><b>Span of time processing</b>For the time required to evaluate the current or future state of health or your request to remove your data.</li>" +
                "<li><b>Legal framework</b>According to the powers provided by Law 132/1988 for the processing clinical laboratory examinations in patients.</li>" +
                "<li><b>Recipients to whom the medical data may be release</b>After your consent the data can be released to your doctor and healthcare professionals who are responsible for your health. Under no circumstances do we expose your personal data to third parties other than your explicit consent.</li></ul></body></html>";

        html1 = "<html><body style=\"font-family:arial\"><ul><li><b>Σκοπός επεξεργασίας των δεδομένων</b>Οι σκοποί για τους οποίους η S.Y Diagnosis Laboratory Center Limited (Diagnosis) συλλέγει, επεξεργάζεται και διατηρεί τα δεδομένα προσωπικού χαρακτήρα σας, αφορούν τις δραστηριότητες μας σε σχέση με την παροχή εργαστηριακών εξετάσεων προς εσάς.</li>" +
                "<li><b>Είδος προσωπικών δεδομένων που υφίστανται ή θα υποστούν επεξεργασία</b><ul><li>Ονοματεπώνυμο</li>" +
                "<li>Ημερομηνία γέννησης</li>"+
                "<li>Αριθμός Ταυτότητας ή διαβατηρίου</li>" +
                "<li>Στοιχεία επικοινωνίας όπως διεύθυνση, τηλέφωνο, τηλεομοιότυπο και ηλεκτρονική διεύθυνση</li></br>" +
                "<li>Πληροφορίες σε σχέση με προηγούμενες συναλλαγές σας με εμάς</li>" +
                "<li>Τρέχουσες θεραπείες</li>" +
                "<li>Αποτελέσματα εξετάσεων</li></ul>" +
                "</li><li><b>Ενημέρωση – Αποθήκευση δεδομένων</b></li>" +
                "<ul><li>Τα αρχεία μας θα ενημερώνονται περιοδικώς με την ίδια μέθοδο με την οποία έχουν συλλεχθεί</li><li>Τα δεδομένα προσωπικού χαρακτήρα σας αποθηκεύονται στα αρχεία μας σε έντυπη και ηλεκτρονική μορφή και φυλάσσονται στις εγκαταστάσεις μας ή και εκτός και το προσωπικό μας έχει πρόσβαση σε αυτά. Έχουμε εγκατεστημένα μέτρα ασφαλείας που διασφαλίζουν την εμπιστευτικότητα των δεδομένων και αυτά τα μέτρα ασφαλείας υπόκεινται σε συνεχή αναθεώρηση και αναβάθμιση.</li></ul></li>" +
                "<li><b>Χρονικό διάστημα για το οποίο θα εκτελείται η επεξεργασία</b>Για το χρονικό διάστημα που απαιτείται για αξιολόγηση  της τρέχουσας ή μεταγενέστερης κατάστασης της υγείας σας ή μετά από ρητό αίτημά σας για κατάργηση των δεδομένων σας,</li>" +
                "<li><b>Νομικό πλαίσιο</b>Σύμφωνα με τις εξουσίες που παρέχει ο Νόμος 132/1988 για την διεκπεραίωση κλινικών εργαστηριακών εξετάσεων σε ασθενείς,</li>" +
                "<li><b>Αποδέκτες στους οποίους ενδέχεται να ανακοινώνονται τα δεδομένα</b>Μετά τη συγκατάθεσή σας, στο γιατρό σας και στους επαγγελματίες υγείας  που είναι υπεύθυνοι της υγείας σας. Σε καμία περίπτωση δεν εκθέτουμε τα προσωπικά σας δεδομένα σε τρίτους εκτός από τη ρητή συγκατάθεσή σας.</li></ul></body></html>";
        webview1.loadDataWithBaseURL(null, MainActivity.this.getResources().getString(R.string.html1), "text/html", "utf-8", null);

        html_en2 = "<html><body style=\"font-family:arial\"><ul><li>the portability of my personal data, the submission of a written request  to access and correct my personal data, to limit the processing data or oppose to processing and deleting my personal data</li>" +
                "<li>The revoke of my consent at any time with submitting a written request to the Data protection Officer of Diagnosis, 36 Vasileos Constantinou Street, 8021 Paphos</li>" +
                "<li>Submitting a complaint to the Office of the Commissioner for the Protection of Personal Data:</br>" +
                "Address: Iasonos 1, 1082 Nicosia</br>" +
                "Phone: 22818456</br>" +
                "Fax: 22304565</br>" +
                "Email: commissioner@dataprotection.gov.cy</li></ul></body></html>";

        html2 = "<html><body style=\"font-family:arial\"><ul><li>στην φορητότητα των δεδομένων προσωπικού μου χαρακτήρα, στην υποβολή γραπτού μηνύματος για πρόσβαση και διόρθωση των δεδομένων προσωπικού μου χαρακτήρα, για περιορισμό της επεξεργασίας των δεδομένων ή εναντίωσης στην επεξεργασία και για διαγραφή των δεδομένων προσωπικού μου χαρακτήρα.</li>" +
                "<li>στην ανάκληση της συγκατάθεσης μου καθ’ οιονδήποτε χρόνο με την υποβολή γραπτού αιτήματος στον Υπεύθυνο Προστασίας Δεδομένων της Diagnosis στην διεύθυνση Βασιλέως Κωνσταντίνου 36, 8021, Πάφος ή στην ηλεκτρονική διεύθυνση info@diagnosislabcenter.com</li>" +
                "<li>στην υποβολή καταγγελίας στο Γραφείο Επιτρόπου Προστασίας Προσωπικών Δεδομένων:</br>" +
                "Διεύθυνση: Ιάσωνος 1, 1082, Λευκωσία</br>" +
                "Τηλέφωνο: 22818456</br>" +
                "Τηλεομοιότυπο: 22304565</br>" +
                "Ηλεκτρονική Διεύθυνση: commissioner@dataprotection.gov.cy</li></ul></body></html>";

        webview2.loadDataWithBaseURL(null, MainActivity.this.getResources().getString(R.string.html2), "text/html", "utf-8", null);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (name.getText().toString().isEmpty()|| birthdate.getText().toString().isEmpty() ||
                        phone.getText().toString().isEmpty() || tk.getText().toString().isEmpty() ||
                        city.getText().toString().isEmpty() || datetime.getText().toString().isEmpty() ||
                        address.getText().toString().isEmpty() || adt.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Συμπλήρωσε όλα τα στοιχεία", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (isValidEmail(email.getText()) == false){
                    Toast.makeText(MainActivity.this,"Η ηλεκτρονική σας διεύθυνση δεν ειναι σωστή", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d("Dimitra",webview1.toString());

                try {
                    viewpdf.setEnabled(true);
                    sendemail.setEnabled(true);
                    if (flag == 1)
                        createPdfen();
                    else if (flag == 2)
                        createPdfgr();
                    else
                        createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        sendemail.setEnabled(false);
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailintent = new Intent(Intent.ACTION_SEND/*, Uri.fromParts("mailto","commissioner@dataprotection.gov.cy",null)*/);
                emailintent.putExtra(Intent.EXTRA_EMAIL, new String[] {email.getText().toString()});
                emailintent.putExtra(Intent.EXTRA_SUBJECT,"Send Pdf");
                emailintent.putExtra(Intent.EXTRA_TEXT, "Diagnosis GDPR PDF file");
                Uri uri = Uri.parse("file://" +myFile.getAbsolutePath());
                emailintent.putExtra(Intent.EXTRA_STREAM, uri);
                emailintent.setType("message/rfc822");
                startActivity(emailintent);

            }
        });

        viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf();
            }
        });

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(MainActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                save.setEnabled(true);
            }

            @Override
            public void onClear() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.en:
                Toast.makeText(MainActivity.this, "Selected options menu item: About", Toast.LENGTH_SHORT).show();
                text1.setText("Privacy Statement Regulation");
                webview1.loadDataWithBaseURL(null,html_en1,"text/html", "utf-8", null);
                text2.setText("CONSENT");
                text3.setText("I confirm that I have read and fully understood the above information.");
                text4.setText("I confirm that I voluntarily consent to the collection, processing and maintenance of my personal data by Diagnosis for purposes as outlined in paragraph 1 above.");
                text5.setText("I confirm that I am aware of and understand my rights regarding:");
                webview2.loadDataWithBaseURL(null,html_en2,"text/html","utf-8",null);
                text6.setText("Name:");
                text7.setText("Birthdate");
                text8.setText("Identify");
                text9.setText("Phone Number");
                text10.setText("Address:");
                text11.setText("Postal Code:");
                text12.setText("City:");
                text13.setText("Email:");
                text14.setText("Signature:");
                text15.setText("Date:");
                text16.setText("Fax");
                text17.setText("Security");
                flag = 1;
                return true;
            case R.id.gr:
                Toast.makeText(MainActivity.this, "Selected options menu item: Help", Toast.LENGTH_SHORT).show();
                text1.setText("Κανονισμός Ρύθμισης Προστασίας Δεδομένων Προσωπικού Χαρακτήρα");
                webview1.loadDataWithBaseURL(null,html1,"text/html","utf-8",null);
                text2.setText("ΣΥΓΚΑΤΑΘΕΣΗ");
                text3.setText("Επιβεβαιώνω ότι έχω διαβάσει και πλήρως κατανοήσει τις πιο πάνω πληροφορίες.");
                text4.setText("Επιβεβαιώνω ότι συγκατατίθεμαι οικειοθελώς στη συλλογή, επεξεργασία και διατήρηση δεδομένων προσωπικού μου χαρακτήρα από την Diagnosis για σκοπούς όπως αναφέρονται στην παράγραφο 1 πιο πάνω.");
                text5.setText("Επιβεβαιώνω ότι έχω πληροφορηθεί και κατανοήσει τα δικαιώματα μου που αφορούν");
                webview2.loadDataWithBaseURL(null,html2,"text/html","utf-8",null);
                text6.setText("Όνομα Επώνυμο:");
                text7.setText("Ημερομηνία Γεννήσης:");
                text8.setText("Α.Δ.Τ:");
                text9.setText("Αριθμός Τηλεφώνου:");
                text10.setText("Διεύθυνση:");
                text11.setText("Τ.Κ:");
                text12.setText("Πόλη:");
                text13.setText("Ηλεκτρονική Διεύθυνση:");
                text14.setText("Υπογραφή");
                text15.setText("Ημερομηνία");
                text16.setText("Φαξ");
                text17.setText("Ασφάλεια");
                flag = 2;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DatePickerDialog.OnDateSetListener birth = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(birthdate,birthCalendar);
        }

    };
    DatePickerDialog.OnDateSetListener currentdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, monthOfYear);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(datetime,dateCalendar);
        }

    };

    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    private void createPdfen() throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.d("Dimitra", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder.getAbsolutePath(), timeStamp + ".pdf");

        //myFile = new File(pdfFolder,timeStamp + ".pdf");
        if (myFile.exists ()) myFile.delete ();
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream output = new FileOutputStream(myFile);


        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        //Step 4 Add content

        try {
            //document.open();
            Drawable d = getResources().getDrawable(R.drawable.logogdpr);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );

        BaseFont bfTimes = null;
        try {
            bfTimes = BaseFont.createFont("assets/arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font urFontName = new Font(bfTimes, 14, Font.BOLD);
        Font titlefont = new Font(bfTimes,18, Font.BOLD, BaseColor.WHITE);

        Paragraph paragraph1 = new Paragraph("Privacy Statement Regulation",urFontName);
        document.add(paragraph1);

        document.add( Chunk.NEWLINE );
        //document.add( Chunk.NEWLINE );

        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Purpose of data processing\n" +
                "The purpose S.Y.Diagnosis Laboratory Center Limited (Diagnosis) collects, processes and maintains personal data, is due to our activities in relation to the laboratory tests concerning you.",urFontName));
        orderedList.add(new ListItem("Type of personal data that they are or will be subjected to processing",urFontName));
        List unorderedList = new List(List.UNORDERED);
        unorderedList.add(new ListItem("Name and Surname",urFontName));
        unorderedList.add(new ListItem("Birthdate",urFontName));
        unorderedList.add(new ListItem("Identity and/or passport number",urFontName));
        unorderedList.add(new ListItem("Contact details such as address, phone, fax and e-mail.",urFontName));
        unorderedList.add(new ListItem("Information relating to your previous transactions with the Lab",urFontName));
        unorderedList.add(new ListItem("Current treatments",urFontName));
        unorderedList.add(new ListItem("Test results",urFontName));
        orderedList.add(unorderedList);

        orderedList.add(new ListItem("Update / Saved data",urFontName));
        List unorderedlist2 = new List(List.UNORDERED);
        unorderedlist2.setFirst(4);
        unorderedlist2.add(new ListItem("Our records will be periodically updated in the same way as which information have been collected,",urFontName));
        unorderedlist2.add(new ListItem("Your personal data is stored in the secure files in printed and electronic form and kept on our facilities.  Only our staff has access to the files. We have security measures installed to ensure the data confidentially and these security measures are subject to constant review and upgrade.",urFontName));
        orderedList.add(unorderedlist2);

        orderedList.add(new ListItem("Span of time processing\n" +
                "For the time required to evaluate the current or future state of health or your request to remove your data.",urFontName));
        orderedList.add(new ListItem("Legal framework\n" +
                "According to the powers provided by Law 132/1988 for the processing clinical laboratory examinations in patients.",urFontName));

        orderedList.add(new ListItem("Recipients to whom the medical data may be release\n" +
                "After your consent the data can be released to your doctor and healthcare professionals who are responsible for your health. Under no circumstances do we expose your personal data to third parties other than your explicit consent.",urFontName));
        document.add(orderedList);

        //document.add( Chunk.NEWLINE );

        document.add(new Paragraph("Concent",urFontName));

        document.add( Chunk.NEWLINE );

        Image tick1 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick1 = Image.getInstance(stream.toByteArray());
            tick1.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk1 = new Chunk(tick1,0,0);
        Chunk confirm1 = new Chunk("I confirm that I have read and fully understood the above information.",urFontName);
        Paragraph par1 = new Paragraph();
        par1.add(chunk1); par1.add(confirm1);
        document.add(par1);

        document.add( Chunk.NEWLINE );

        Image tick2 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick2 = Image.getInstance(stream.toByteArray());
            tick2.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk2 = new Chunk(tick2,0,0);
        Chunk confirm2 = new Chunk("I confirm that I voluntarily consent to the collection, processing and maintenance of my personal data by Diagnosis for purposes as outlined in paragraph 1 above.",urFontName);
        Paragraph par2 = new Paragraph();
        par2.add(chunk2); par2.add(confirm2);
        document.add(par2);

        document.add( Chunk.NEWLINE );

        Image tick3 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick3 = Image.getInstance(stream.toByteArray());
            tick3.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk3 = new Chunk(tick3,0,0);
        Chunk confirm3 = new Chunk("I confirm that I am aware of and understand my rights regarding:",urFontName);
        Paragraph par3 = new Paragraph();
        par3.add(chunk3); par3.add(confirm3);
        document.add(par3);

        List orderedlist2 = new List(List.UNORDERED);
        orderedlist2.setFirst(2);
        orderedlist2.add(new ListItem("the portability of my personal data, the submission of a written request  to access and correct my personal data, to limit the processing data or oppose to processing and deleting my personal data",urFontName));
        orderedlist2.add(new ListItem("The revoke of my consent at any time with submitting a written request to the Data protection Officer of Diagnosis, 36 Vasileos Constantinou Street, 8021 Paphos",urFontName));
        orderedlist2.add(new ListItem("Submitting a complaint to the Office of the Commissioner for the Protection of Personal Data:\n" +
                "Address: Iasonos 1, 1082 Nicosia\n" +
                "Phone: 22818456\n" +
                "Fax: 22304565\n" +
                "\tEmail: commissioner@dataprotection.gov.cy",urFontName));
        document.add(orderedlist2);

        document.add( Chunk.NEWLINE );


        PdfPTable pdfPTable ;
        PdfPCell cell;

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        String myname=name.getText().toString();
        Chunk ch1 = new Chunk("                                  Name",titlefont);
        Chunk name = new Chunk("     "+myname,urFontName);

        Paragraph p1 = new Paragraph(ch1);
        cell = new PdfPCell(p1);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(name));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch3 = new Chunk("                                  Birthdate",titlefont);
        Chunk birth = new Chunk("    "+birthdate.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch3));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        cell = new PdfPCell(new Paragraph(birth));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch5 = new Chunk("                             Indentify",titlefont);
        Chunk indentify = new Chunk("     "+adt.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch5));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(indentify));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);



        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch7 = new Chunk("                       Phone Number",titlefont);
        Chunk phonenum = new Chunk("     "+phone.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch7));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(phonenum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch10 = new Chunk("                       Fax Number",titlefont);
        Chunk faxnum = new Chunk("     "+fax.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(faxnum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch16 = new Chunk("                       Security",titlefont);
        Chunk asfaleia = new Chunk("     "+fax.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch16));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(asfaleia));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);



        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch9 = new Chunk("                                  Address",titlefont);
        Chunk add = new Chunk("     "+address.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch9));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(add));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch11 = new Chunk("                            Postal Code",titlefont);
        Chunk postalcode = new Chunk("     "+tk.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch11));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(postalcode));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch13 = new Chunk("                                         City",titlefont);
        Chunk mycity = new Chunk("     "+city.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch13));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(mycity));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch15 = new Chunk("                                    Email",titlefont);
        Chunk myemail = new Chunk("     "+email.getText().toString(),urFontName);
        myemail.setUnderline(1.7f, -2);
        cell = new PdfPCell(new Paragraph(ch15));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(myemail));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch19 = new Chunk("                                        Date",titlefont);
        Chunk currentdate = new Chunk("     "+datetime.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch19));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(currentdate));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);

        signatureBitmap = mSignaturePad.getSignatureBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        Image sign = null;
        try {
            sign = Image.getInstance(stream.toByteArray());
            sign.scaleAbsolute(200,100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch17 = new Chunk("                                Signature",titlefont);

        cell = new PdfPCell(new Paragraph(ch17));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        Chunk ch18 = new Chunk(sign,0,-110f);
        Paragraph paragraph12 = new Paragraph();
        paragraph12.add(ch18);
        cell = new PdfPCell(paragraph12);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);

        //Step 5: Close the document
        document.close();

    }

    private void createPdfgr() throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.d("Dimitra", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder.getAbsolutePath(), timeStamp + ".pdf");

        //myFile = new File(pdfFolder,timeStamp + ".pdf");
        if (myFile.exists ()) myFile.delete ();
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream output = new FileOutputStream(myFile);


        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        //Step 4 Add content

        try {
            //document.open();
            Drawable d = getResources().getDrawable(R.drawable.logogdpr);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );

        BaseFont bfTimes = null;
        try {
            bfTimes = BaseFont.createFont("assets/arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font urFontName = new Font(bfTimes, 14, Font.BOLD);
        Font titlefont = new Font(bfTimes,18, Font.BOLD, BaseColor.WHITE);

        Paragraph paragraph1 = new Paragraph("Κανονισμός Ρύθμισης Προστασίας Δεδομένων Προσωπικού Χαρακτήρα",urFontName);
        document.add(paragraph1);

        document.add( Chunk.NEWLINE );
        //document.add( Chunk.NEWLINE );

        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Σκοπός επεξεργασίας των δεδομένων\n" +
                "Οι σκοποί για τους οποίους η S.Y Diagnosis Laboratory Center Limited (Diagnosis) συλλέγει, επεξεργάζεται και διατηρεί τα δεδομένα προσωπικού χαρακτήρα σας, αφορούν τις δραστηριότητες μας σε σχέση με την παροχή εργαστηριακών εξετάσεων προς εσάς.",urFontName));
        orderedList.add(new ListItem("Είδος προσωπικών δεδομένων που υφίστανται ή θα υποστούν επεξεργασία",urFontName));
        List unorderedList = new List(List.UNORDERED);
        unorderedList.add(new ListItem("Ονοματεπώνυμο",urFontName));
        unorderedList.add(new ListItem("Ημερομηνία γέννησης",urFontName));
        unorderedList.add(new ListItem("Αριθμός Ταυτότητας ή διαβατηρίου ",urFontName));
        unorderedList.add(new ListItem("Στοιχεία επικοινωνίας όπως διεύθυνση, τηλέφωνο, τηλεομοιότυπο και ηλεκτρονική διεύθυνση",urFontName));
        unorderedList.add(new ListItem("Πληροφορίες σε σχέση με προηγούμενες συναλλαγές σας με εμάς",urFontName));
        unorderedList.add(new ListItem("Τρέχουσες θεραπείες",urFontName));
        unorderedList.add(new ListItem("Αποτελέσματα εξετάσεων",urFontName));
        orderedList.add(unorderedList);

        orderedList.add(new ListItem("Ενημέρωση – Αποθήκευση δεδομένων",urFontName));
        List unorderedlist2 = new List(List.UNORDERED);
        unorderedlist2.setFirst(4);
        unorderedlist2.add(new ListItem("Τα αρχεία μας θα ενημερώνονται περιοδικώς με την ίδια μέθοδο με την οποία έχουν συλλεχθεί,",urFontName));
        unorderedlist2.add(new ListItem("Τα δεδομένα προσωπικού χαρακτήρα σας αποθηκεύονται στα αρχεία μας σε έντυπη και ηλεκτρονική μορφή και φυλάσσονται στις εγκαταστάσεις μας ή και εκτός και το προσωπικό μας έχει πρόσβαση σε αυτά. Έχουμε εγκατεστημένα μέτρα ασφαλείας που διασφαλίζουν την εμπιστευτικότητα των δεδομένων και αυτά τα μέτρα ασφαλείας υπόκεινται σε συνεχή αναθεώρηση και αναβάθμιση.",urFontName));
        orderedList.add(unorderedlist2);

        orderedList.add(new ListItem("Χρονικό διάστημα για το οποίο θα εκτελείται η επεξεργασία\n" +
                "Για το χρονικό διάστημα που απαιτείται για αξιολόγηση  της τρέχουσας ή μεταγενέστερης κατάστασης της υγείας σας ή μετά από ρητό αίτημά σας για κατάργηση των δεδομένων σας,",urFontName));
        orderedList.add(new ListItem("Νομικό πλαίσιο\n" +
                "Σύμφωνα με τις εξουσίες που παρέχει ο Νόμος 132/1988 για την διεκπεραίωση κλινικών εργαστηριακών εξετάσεων σε ασθενείς,",urFontName));

        orderedList.add(new ListItem("Αποδέκτες στους οποίους ενδέχεται να ανακοινώνονται τα δεδομένα\n" +
                "Μετά τη συγκατάθεσή σας, στο γιατρό σας και στους επαγγελματίες υγείας  που είναι υπεύθυνοι της υγείας σας. Σε καμία περίπτωση δεν εκθέτουμε τα προσωπικά σας δεδομένα σε τρίτους εκτός από τη ρητή συγκατάθεσή σας.",urFontName));
        document.add(orderedList);

        //document.add( Chunk.NEWLINE );

        document.add(new Paragraph("ΣΥΓΚΑΤΑΘΕΣΗ",urFontName));

        document.add( Chunk.NEWLINE );

        Image tick1 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick1 = Image.getInstance(stream.toByteArray());
            tick1.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk1 = new Chunk(tick1,0,0);
        Chunk confirm1 = new Chunk("Επιβεβαιώνω ότι έχω διαβάσει και πλήρως κατανοήσει τις πιο πάνω πληροφορίες.",urFontName);
        Paragraph par1 = new Paragraph();
        par1.add(chunk1); par1.add(confirm1);
        document.add(par1);

        document.add( Chunk.NEWLINE );

        Image tick2 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick2 = Image.getInstance(stream.toByteArray());
            tick2.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk2 = new Chunk(tick2,0,0);
        Chunk confirm2 = new Chunk("Επιβεβαιώνω ότι συγκατατίθεμαι οικειοθελώς στη συλλογή, επεξεργασία και διατήρηση δεδομένων προσωπικού μου χαρακτήρα από την Diagnosis για σκοπούς όπως αναφέρονται στην παράγραφο 1 πιο πάνω.",urFontName);
        Paragraph par2 = new Paragraph();
        par2.add(chunk2); par2.add(confirm2);
        document.add(par2);

        document.add( Chunk.NEWLINE );

        Image tick3 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick3 = Image.getInstance(stream.toByteArray());
            tick3.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk3 = new Chunk(tick3,0,0);
        Chunk confirm3 = new Chunk("Επιβεβαιώνω ότι έχω πληροφορηθεί και κατανοήσει τα δικαιώματα μου που αφορούν",urFontName);
        Paragraph par3 = new Paragraph();
        par3.add(chunk3); par3.add(confirm3);
        document.add(par3);

        List orderedlist2 = new List(List.UNORDERED);
        orderedlist2.setFirst(2);
        orderedlist2.add(new ListItem("στην φορητότητα των δεδομένων προσωπικού μου χαρακτήρα, στην υποβολή γραπτού μηνύματος για πρόσβαση και διόρθωση των δεδομένων προσωπικού μου χαρακτήρα, για περιορισμό της επεξεργασίας των δεδομένων ή εναντίωσης στην επεξεργασία και για διαγραφή των δεδομένων προσωπικού μου χαρακτήρα.",urFontName));
        orderedlist2.add(new ListItem("στην ανάκληση της συγκατάθεσης μου καθ’ οιονδήποτε χρόνο με την υποβολή γραπτού αιτήματος στον Υπεύθυνο Προστασίας Δεδομένων της Diagnosis στην διεύθυνση Βασιλέως Κωνσταντίνου 36, 8021, Πάφος ή στην ηλεκτρονική διεύθυνση info@diagnosislabcenter.com",urFontName));
        orderedlist2.add(new ListItem("στην υποβολή καταγγελίας στο Γραφείο Επιτρόπου Προστασίας Προσωπικών Δεδομένων:\n" +
                "Διεύθυνση: Ιάσωνος 1, 1082, Λευκωσία\n" +
                "Τηλέφωνο: 22818456\n" +
                "Τηλεομοιότυπο: 22304565\n" +
                "\tΗλεκτρονική Διεύθυνση: commissioner@dataprotection.gov.cy",urFontName));
        document.add(orderedlist2);

        document.add( Chunk.NEWLINE );


        PdfPTable pdfPTable ;
        PdfPCell cell;

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        String myname=name.getText().toString();
        Chunk ch1 = new Chunk("                      Ονοματεπώνυμο",titlefont);
        Chunk name = new Chunk("     "+myname,urFontName);

        Paragraph p1 = new Paragraph(ch1);
        cell = new PdfPCell(p1);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(name));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch3 = new Chunk("          Ημερομηνία Γέννησης",titlefont);
        Chunk birth = new Chunk("    "+birthdate.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch3));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        cell = new PdfPCell(new Paragraph(birth));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch5 = new Chunk("                            Ταυτότητα",titlefont);
        Chunk indentify = new Chunk("     "+adt.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch5));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(indentify));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch7 = new Chunk("                               Τηλέφωνο",titlefont);
        Chunk phonenum = new Chunk("     "+phone.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch7));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(phonenum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);


        Chunk ch12 = new Chunk("                                 Φαξ",titlefont);
        Chunk faxnum = new Chunk("     "+fax.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch12));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(faxnum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);


        Chunk ch14 = new Chunk("                               Ασφάλεια",titlefont);
        Chunk security = new Chunk("     "+asfalia.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch14));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(security));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);



        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch9 = new Chunk("                             Διεύθηνση",titlefont);
        Chunk add = new Chunk("     "+address.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch9));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(add));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch11 = new Chunk("                                        Τ.Κ",titlefont);
        Chunk postalcode = new Chunk("     "+tk.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch11));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(postalcode));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch13 = new Chunk("                                     Πόλη",titlefont);
        Chunk mycity = new Chunk("     "+city.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch13));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(mycity));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch15 = new Chunk("                                      Email",titlefont);
        Chunk myemail = new Chunk("     "+email.getText().toString(),urFontName);
        myemail.setUnderline(1.7f, -2);
        cell = new PdfPCell(new Paragraph(ch15));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(myemail));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch19 = new Chunk("                              Ημερομηνία",titlefont);
        Chunk currentdate = new Chunk("     "+datetime.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch19));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(currentdate));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);

        signatureBitmap = mSignaturePad.getSignatureBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        Image sign = null;
        try {
            sign = Image.getInstance(stream.toByteArray());
            sign.scaleAbsolute(200,100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch17 = new Chunk("                              Υπογραφή",titlefont);

        cell = new PdfPCell(new Paragraph(ch17));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        Chunk ch18 = new Chunk(sign,0,-110f);
        Paragraph paragraph12 = new Paragraph();
        paragraph12.add(ch18);
        cell = new PdfPCell(paragraph12);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        //Step 5: Close the document
        document.close();

    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.d("Dimitra", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder.getAbsolutePath(), timeStamp + ".pdf");

        //myFile = new File(pdfFolder,timeStamp + ".pdf");
        if (myFile.exists ()) myFile.delete ();
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream output = new FileOutputStream(myFile);


        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        //Step 4 Add content

        try {
            //document.open();
            Drawable d = getResources().getDrawable(R.drawable.logogdpr);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );

        BaseFont bfTimes = null;
        try {
            bfTimes = BaseFont.createFont("assets/arial.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font urFontName = new Font(bfTimes, 14, Font.BOLD);
        Font titlefont = new Font(bfTimes,18, Font.BOLD, BaseColor.WHITE);

        Paragraph paragraph1 = new Paragraph(getString(R.string.content),urFontName);
        document.add(paragraph1);

        document.add( Chunk.NEWLINE );
        //document.add( Chunk.NEWLINE );

        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem(getString(R.string.html1_l1),urFontName));
        orderedList.add(new ListItem(getString(R.string.html1_l2),urFontName));
        List unorderedList = new List(List.UNORDERED);
        unorderedList.add(new ListItem(getString(R.string.html1_l3),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l4),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l5),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l6),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l8),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l9),urFontName));
        unorderedList.add(new ListItem(getString(R.string.html1_l10),urFontName));
        orderedList.add(unorderedList);

        orderedList.add(new ListItem(getString(R.string.html1_l11),urFontName));
        List unorderedlist2 = new List(List.UNORDERED);
        unorderedlist2.setFirst(4);
        unorderedlist2.add(new ListItem(getString(R.string.html1_l12),urFontName));
        unorderedlist2.add(new ListItem(getString(R.string.html1_l13),urFontName));
        orderedList.add(unorderedlist2);

        orderedList.add(new ListItem(getString(R.string.html1_l14),urFontName));
        orderedList.add(new ListItem(getString(R.string.html1_l15),urFontName));

        orderedList.add(new ListItem("Αποδέκτες στους οποίους ενδέχεται να ανακοινώνονται τα δεδομένα\n" +
                "Μετά τη συγκατάθεσή σας, στο γιατρό σας και στους επαγγελματίες υγείας  που είναι υπεύθυνοι της υγείας σας. Σε καμία περίπτωση δεν εκθέτουμε τα προσωπικά σας δεδομένα σε τρίτους εκτός από τη ρητή συγκατάθεσή σας.",urFontName));
        document.add(orderedList);

        //document.add( Chunk.NEWLINE );

        document.add(new Paragraph(getString(R.string.sigatathesi),urFontName));

        document.add( Chunk.NEWLINE );

        Image tick1 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick1 = Image.getInstance(stream.toByteArray());
            tick1.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk1 = new Chunk(tick1,0,0);
        Chunk confirm1 = new Chunk(getString(R.string.epivevaiosi1),urFontName);
        Paragraph par1 = new Paragraph();
        par1.add(chunk1); par1.add(confirm1);
        document.add(par1);

        document.add( Chunk.NEWLINE );

        Image tick2 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick2 = Image.getInstance(stream.toByteArray());
            tick2.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk2 = new Chunk(tick2,0,0);
        Chunk confirm2 = new Chunk(getString(R.string.epivevaiosi2),urFontName);
        Paragraph par2 = new Paragraph();
        par2.add(chunk2); par2.add(confirm2);
        document.add(par2);

        document.add( Chunk.NEWLINE );

        Image tick3 = null;
        try {
            Drawable d = getResources().getDrawable(R.drawable.blue_tick);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            tick3 = Image.getInstance(stream.toByteArray());
            tick3.scaleAbsolute(30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Chunk chunk3 = new Chunk(tick3,0,0);
        Chunk confirm3 = new Chunk(getString(R.string.epivevaiosi3),urFontName);
        Paragraph par3 = new Paragraph();
        par3.add(chunk3); par3.add(confirm3);
        document.add(par3);

        List orderedlist2 = new List(List.UNORDERED);
        orderedlist2.setFirst(2);
        orderedlist2.add(new ListItem(getString(R.string.html2_l1),urFontName));
        orderedlist2.add(new ListItem(getString(R.string.html1_l2),urFontName));
        orderedlist2.add(new ListItem(getString(R.string.html1_l3) +"\n"+
                getString(R.string.html1_l4)+"\n" +
                getString(R.string.html1_l5)+"\n" +
                getString(R.string.html1_l6)+"\n" +
                "\t"+getString(R.string.html1_l7),urFontName));
        document.add(orderedlist2);

        document.add( Chunk.NEWLINE );


        PdfPTable pdfPTable ;
        PdfPCell cell;

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        String myname=name.getText().toString();
        Chunk ch1 = new Chunk("                  "+getResources().getString(R.string.onomateponymo),titlefont);
        Chunk name = new Chunk("     "+myname,urFontName);

        Paragraph p1 = new Paragraph(ch1);
        cell = new PdfPCell(p1);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(name));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch3 = new Chunk("       "+getResources().getString(R.string.hmeromhniagenisis),titlefont);
        Chunk birth = new Chunk("    "+birthdate.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch3));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        cell = new PdfPCell(new Paragraph(birth));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch5 = new Chunk("                     "+getResources().getString(R.string.adt),titlefont);
        Chunk indentify = new Chunk("     "+adt.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch5));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(indentify));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch7 = new Chunk("                     "+getResources().getString(R.string.tilephono),titlefont);
        Chunk phonenum = new Chunk("     "+phone.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch7));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(phonenum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch12 = new Chunk("                     "+getResources().getString(R.string.fax),titlefont);
        Chunk faxnum = new Chunk("     "+fax.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch12));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(faxnum));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch14 = new Chunk("                     "+getResources().getString(R.string.asfaleia),titlefont);
        Chunk security = new Chunk("     "+asfalia.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch14));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(security));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch9 = new Chunk("                     "+getResources().getString(R.string.diefthinsi),titlefont);
        Chunk add = new Chunk("     "+address.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch9));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(add));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch11 = new Chunk("                         "+getResources().getString(R.string.tk),titlefont);
        Chunk postalcode = new Chunk("     "+tk.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch11));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(postalcode));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch13 = new Chunk("                        "+getResources().getString(R.string.poli),titlefont);
        Chunk mycity = new Chunk("     "+city.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch13));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(mycity));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);
        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch15 = new Chunk("         "+getResources().getString(R.string.email),titlefont);
        Chunk myemail = new Chunk("     "+email.getText().toString(),urFontName);
        myemail.setUnderline(1.7f, -2);
        cell = new PdfPCell(new Paragraph(ch15));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(myemail));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch19 = new Chunk("                        "+getResources().getString(R.string.hmeromhnia),titlefont);
        Chunk currentdate = new Chunk("     "+datetime.getText().toString(),urFontName);

        cell = new PdfPCell(new Paragraph(ch19));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Paragraph(currentdate));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(70f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);

        signatureBitmap = mSignaturePad.getSignatureBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        Image sign = null;
        try {
            sign = Image.getInstance(stream.toByteArray());
            sign.scaleAbsolute(200,100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfPTable = new PdfPTable(2);
        pdfPTable.setTotalWidth(500);
        pdfPTable.setLockedWidth(true);

        Chunk ch17 = new Chunk("                       "+getString(R.string.ypografh),titlefont);

        cell = new PdfPCell(new Paragraph(ch17));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new  BaseColor(70,130,180));
        cell.setCellEvent(new DottedCell(PdfPCell.LEFT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        Chunk ch18 = new Chunk(sign,0,-110f);
        Paragraph paragraph12 = new Paragraph();
        paragraph12.add(ch18);
        cell = new PdfPCell(paragraph12);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        cell.setBorderWidthBottom(2);
        cell.setBorderWidthTop(2);
        cell.setBorderWidthLeft(2);
        cell.setBorderWidthRight(2);
        cell.setFixedHeight(150f);
        cell.setBorderColor(BaseColor.BLACK);
        pdfPTable.addCell(cell);

        document.add(pdfPTable);


        //Step 5: Close the document
        document.close();

    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri URI = Uri.parse("file://" + myFile.getAbsolutePath());
        intent.setDataAndType(URI,"application/pdf");;
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MainActivity.this, "Permission denied to access your location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
