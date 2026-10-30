package pt.uptomar.dam2026.datastorage

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import pt.uptomar.dam2026.datastorage.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.Scanner


class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        viewBinding= ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tarefa 1
        viewBinding.btWrite1.setOnClickListener {
            // escreve dados na Shared Preferences
            escreveSharedPreferences()
            // esconder teclado
            escondeTeclado(it)
        }
        viewBinding.btRead1.setOnClickListener {
            // ler os dados guardados na Shared Preferences
            lerSharedPreferences()
        }

        // Tarefa 2 - Shared Preferences com múltiplos ficheiros
        viewBinding.btWrite2.setOnClickListener {
            // escreve dados na Shared Preferences com múltiplos ficheiros
            escreveSharedPreferencesMultiFich()
            // esconder teclado
            escondeTeclado(it)
        }
        viewBinding.btRead2.setOnClickListener {
            // ler os dados guardados na Shared Preferences com múltiplos ficheiros
            lerSharedPreferencesMultiFich()
        }


        // Tarefa 3 - Cache
        viewBinding.btWrite3.setOnClickListener {
            // escreve dados na Cache
            escreveCache()
            // esconder teclado
            escondeTeclado(it)
        }
        viewBinding.btRead3.setOnClickListener {
            // ler os dados guardados na Cache
            lerCache()
        }


        // Tarefa 4 - Internal Storage
        viewBinding.btWrite4.setOnClickListener {
            // escreve dados na Internal Storage
            escreveInternalStorage()
            // esconder teclado
            escondeTeclado(it)
        }
        viewBinding.btRead4.setOnClickListener {
            // ler os dados guardados na Internal Storage
            lerInternalStorage()
        }

    }


    /**
     * ler os dados guardados na Internal Storage
     */
    private fun lerInternalStorage() {
        val directory: File = filesDir
        val file = File(directory, "dadosInternalStorage.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            Snackbar.make(
                viewBinding.root,
                "Você chama-se ${
                    nome.toString().uppercase()
                } e tem $idade anos! - Internal Storage",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.internal_storage_vazia), Toast.LENGTH_LONG)
                .show()
        }

    }

    /**
     * escrever dados na Internal Storage
     */
    private fun escreveInternalStorage() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = filesDir
        // nome do ficheiro na Internal Storage
        val file: File = File(directory, "dadosInternalStorage.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(viewBinding.editText4a.text)
            ps.println(viewBinding.editText4b.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.escritaErradaCache), Toast.LENGTH_LONG).show()
        }
    }


    /**
     * Escreve na cache do telemóvel
     */
    private fun escreveCache() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = cacheDir
        // nome do ficheiro na Cache
        val file: File = File(directory, "dadosCache.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(viewBinding.editText3a.text)
            ps.println(viewBinding.editText3b.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.escritaErradaCache), Toast.LENGTH_LONG).show()
        }

    }

    /**
     * lê dados da cache do telemóvel
     */
    private fun lerCache() {
        val directory: File = cacheDir // <=> getCacheDir()
        val file = File(directory, "dadosCache.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            Snackbar.make(
                viewBinding.root,
                "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.cache_vazia), Toast.LENGTH_LONG).show()
        }

    }

    /**
     * ler dados de diversos ficheiros Shared Preferences
     */
    private fun lerSharedPreferencesMultiFich() {
        val sharedPreferencesNome = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdade = getSharedPreferences("idades.dat", MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val nome = sharedPreferencesNome.getString("NOME", getString(R.string.txt_vazio))
        val idade = sharedPreferencesIdade.getInt("IDADE", 0)

        // usar os dados lidos
        Snackbar.make(
            viewBinding.root,
            "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * escrever em Shared Preferences com múltiplos ficheiros
     */
    private fun escreveSharedPreferencesMultiFich() {
        val sharedPreferencesNomes = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdades = getSharedPreferences("idades.dat", MODE_PRIVATE)

        val editorNomes: SharedPreferences.Editor = sharedPreferencesNomes.edit()
        val editorIdades: SharedPreferences.Editor = sharedPreferencesIdades.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editorNomes.putString("NOME", viewBinding.editText2a.text.toString())
        editorIdades.putInt(
            "IDADE",
            viewBinding.editText2b.text.toString().toIntOrNull() ?: 0
        ) // se a Idade=NULL, coloca 0
        // guardar as alterações
        editorNomes.commit()  // eventualmente, <=>   editorNomes.apply()
        editorIdades.commit()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
    }

    /**
     * lê os dados escritos na Shared Preferences
     */
    private fun lerSharedPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val texto = sharedPreferences.getString("SPreferenceSimples", getString(R.string.txt_vazio))

        // usar os dados lidos
        // Snackbar.make(viewBinding.root, "Introduziu: $texto", Snackbar.LENGTH_SHORT).show()
        Snackbar.make(viewBinding.root, getString(R.string.texto,texto), Snackbar.LENGTH_SHORT).show()

    }

    /**
     * Escreve na Shared Preferences os dados escritos pelo utilizador na interface
     */
    private fun escreveSharedPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editor.putString("SPreferenceSimples", viewBinding.editText1.text.toString())
        // guardar as alterações
        editor.commit()  // eventualmente, <=>   editor.apply()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()

    }

    /**
     * esconde o teclado quando já não é necessário
     */
    private fun escondeTeclado(view: View) {
        // hide the keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}