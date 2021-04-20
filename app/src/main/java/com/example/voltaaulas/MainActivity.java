package com.example.voltaaulas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import gaspar.aulas.bancofirebase.R;

public class MainActivity extends AppCompatActivity {
    //Conexão do aplicativo com o Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Declaração dos objetos utilizados na tela
    private EditText edNome, edSobrenome, edIdade, edPeso, edAltura;
    private Button btCadastro, btCadastroChave, btBuscarTodos, btBuscarChave, btRemover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ligação dos objetos com os IDs da tela
        edNome = findViewById(R.id.edNome);
        edSobrenome = findViewById(R.id.edSobrenome);
        edIdade = findViewById(R.id.edIdade);
        edPeso = findViewById(R.id.edPeso);
        edAltura = findViewById(R.id.edAltura);
        btCadastro = findViewById(R.id.btCadastro);
        btCadastroChave = findViewById(R.id.btBuscarTodos);
        btBuscarTodos = findViewById(R.id.btBuscarTodos);
        btBuscarChave = findViewById(R.id.btRemover);
        btRemover = findViewById(R.id.btRemover);

        //Evento do botão cadastrar
        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Montar um objeto da classe Pessoa
                Pessoa p = new Pessoa(edNome.getText().toString(), edSobrenome.getText().toString(),
                        Integer.parseInt(edIdade.getText().toString()),
                        Double.parseDouble(edPeso.getText().toString()),
                        Double.parseDouble(edAltura.getText().toString()));

                db.collection("pessoas")
                        .add(p)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("LOG_FIREBASE", "Cadastrado com ID: " + documentReference.getId());
                                Toast.makeText(MainActivity.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("LOG_FIREBASE", "Erro ao cadastrar", e);
                                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Cadastrnado com o ID Nome e Sobrenome
        btCadastroChave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pessoa p = new Pessoa(edNome.getText().toString(), edSobrenome.getText().toString(),
                        Integer.parseInt(edIdade.getText().toString()),
                        Double.parseDouble(edPeso.getText().toString()),
                        Double.parseDouble(edAltura.getText().toString()));

                //cadastro no firebase com id gerado manualmente
                db.collection("pessoas")
                        //passando o id
                        //Faz com que o id do banco seja Nome e sobrenome
                        .document(edNome.getText().toString() + edSobrenome.getText().toString())
                        .set(p)//Objeto que será cadastrado com o ID acima

                        //Ve se deu certo
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //Ve se deu erro
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        //Buscando todos os cadastrados
        btBuscarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("pessoas")

                        .get() //Get ja recupera todos os registros

                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {//Task e o ressultado da solução

                                /**Verificando se deu certo**/
                                if (task.isSuccessful()) {//Verificando se deu sucesso

                                    /**Testar se ha valores de retorno**/
                                    if (!task.getResult().isEmpty()) {//Testando se ta vazio

                                        /**Para percorre cada registro encontrado**/
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            //Procurando os campos para msotrar
                                            Log.d("DOC_FIRE", doc.getId());
                                            Log.d("DOC_FIRE", doc.getString("nome"));
                                            Log.d("DOC_FIRE", doc.getString("sobrenome"));
                                            Log.d("DOC_FIRE", doc.getLong("idade") + "");
                                            Log.d("DOC_FIRE", doc.getDouble("peso") + "");
                                            Log.d("DOC_FIRE", doc.getDouble("altura") + "");

                                        }
                                    }
                                } else {
                                    Log.d("DOC_FIRE", "Erro ao recuperar" + task.getException());
                                }
                            }
                        });
            }
        });


        //Buscado algum com o id
        btBuscarChave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("pessoas")
                        .document(edNome.getText().toString() + edSobrenome.getText().toString())
                        .get()//pegar o valor que a gente declarou acima

                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    /**Recuperar o documento que a busca encontrou**/
                                    DocumentSnapshot doc = task.getResult();

                                    /**Testar se o documento existe**/
                                    if (doc.exists()) {
                                        Log.d("DOC_FIRE", doc.getId());
                                        Log.d("DOC_FIRE", doc.getString("nome"));
                                        Log.d("DOC_FIRE", doc.getString("sobrenome"));
                                        Log.d("DOC_FIRE", doc.getLong("idade") + "");
                                        Log.d("DOC_FIRE", doc.getDouble("peso") + "");
                                        Log.d("DOC_FIRE", doc.getDouble("altura") + "");
                                    }

                                } else {
                                    Log.d("DOC_FIRE", "Erro ao recuperar" + task.getException());
                                }
                            }
                        });
            }
        });

        //Remover um documento
        btRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("pessoas")
                        .document(edNome.getText().toString() + edSobrenome.getText().toString())
                        //Para deletar
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Removido", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Erro, não foi possivel remover", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }
}