# Zé Delivery Challenge

Projeto para o code-challenge da Zx Ventures.


## Arquitetura

Foi utilizada uma arquitetura baseada na [Android Clean Architecture](https://github.com/android10/Android-CleanArchitecture/), devido à sua fácil compreensão, suporte para testes automatizados e suporte para o crescimento do projeto. 

Temos 3 divisões principais:
- data: camada de acesso aos dados, no caso, acesso às API's em GraphQL.
- presentation: camada de apresentação / interface com usuário, dividida em packages-by-feature.
- domain: camada intermediária entre as duas anteriores, define os models que serão utilizados na camada de apresentação.

Essas divisões facilitam *muito* a manutenção em qualquer camada, pois sempre temos um "contrato" definido pela camada mais externa, fazendo com que possamos por exemplo alterar completamente a camada de `data` sem precisar alterar uma linha de código na camada `presentation`, ou que possamos utilizar cache alterando apenas a camada `domain` sem precisar alterar nenhuma outra.

Uma ótima biblioteca para utilizar em projetos com essa divisão de camadas é a [Dagger](https://google.github.io/dagger/), com ela podemos gerar toda a injeção de dependências usando apenas anotações e algumas configurações, no caso desse projeto ela substituiria a classe PresenterFactory.

Escrevi alguns testes unitários somente para exemplificar como podemos validar lógicas de apresentação e ter uma cobertura de código razoável. Acabei não escrevendo testes para o emulador pois são mais complexos e demorados, mas a arquitetura facilita o mock das outras camadas para podermos simular qualquer estado para validação.


## Bibliotecas utilizadas

- com.android.support.* 
-- bibliotecas de suporte padrão, recycler view, card view, constraint layout, etc.

- Butter Knife
-- auxílio para boilerplate de binds de views.

- RxJava / RxAndroid
-- para moldar a comunicação "baseada em eventos" e facilitar bastante a mudança de threads nas operações assíncronas.

- Apollo
-- melhor biblioteca que encontrei para gerar código de acesso às API's em GraphQL, ela se no schema.json e em arquivos de querys para gerar os códigos de acesso.

- Glide
-- biblioteca da Google para carregamento de imagens.
