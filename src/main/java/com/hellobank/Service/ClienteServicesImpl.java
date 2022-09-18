package com.hellobank.Service;

import java.util.ArrayList;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellobank.DAO.ClienteDAO;
import com.hellobank.Model.Cliente;
import com.hellobank.Service.exception.CpfCnpjObrigatorioClienteException;
import com.hellobank.Service.exception.EmailClienteJaCadastradoException;
import com.hellobank.Service.exception.ObjetoNaoEncontradoException;

import org.springframework.util.StringUtils;

@Service
public class ClienteServicesImpl implements ICliente {


    @Autowired
    private ClienteDAO dao;

    @Override
    public ArrayList<Cliente> buscarTodos() {
        ArrayList<Cliente> lista;
		
		lista=(ArrayList<Cliente>)dao.findAll();
		
		return lista;
    }

    @Override
	@Transactional
    public Cliente salvar(Cliente cliente) {
        Optional<Cliente> clienteExistente = dao.findByEmail(cliente.getEmail());
        if (clienteExistente.isPresent() && !clienteExistente.get().equals(cliente)) {
			throw new EmailClienteJaCadastradoException("E-mail já cadastrado");
		}
        if(cliente.isNovo() && !StringUtils.hasLength(cliente.getCpfCnpj())){
            throw new CpfCnpjObrigatorioClienteException("Cpf/CNPJ é obrigatória para novo cliente");
        } 
        if(cliente.isNovo() && !StringUtils.hasLength(cliente.getSenha())){
            throw new CpfCnpjObrigatorioClienteException("Senha é obrigatória para novo cliente");
        }
        if(cliente.getNome()!=null ) {
			return dao.save(cliente);
		}
		return null;

    }

    @Override
    public Cliente atualizarDados(Cliente cliente) {
        Optional<Cliente> clienteExistente = dao.findByEmail(cliente.getEmail());
        if (clienteExistente.isPresent() && !clienteExistente.get().equals(cliente)) {
			throw new EmailClienteJaCadastradoException("E-mail já cadastrado");
		}
        if(cliente.isNovo() && !StringUtils.hasLength(cliente.getCpfCnpj())){
            throw new CpfCnpjObrigatorioClienteException("Cpf/CNPJ é obrigatória para novo cliente");
        }
        if(cliente.isNovo() && !StringUtils.hasLength(cliente.getSenha())){
            throw new CpfCnpjObrigatorioClienteException("Senha é obrigatória para novo cliente");
        }
        if(cliente.getId()!=null && cliente.getNome()!=null && cliente.getCpfCnpj()!=null) {
			return dao.save(cliente);
		}
		return null;
    }

    @Override
    public Cliente buscarPeloId(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ObjetoNaoEncontradoException("Cliente não encontrado! id: " + id));
    }

    @Override
    public void excluir(Integer id) {
        dao.deleteById(id);
        
    }

    @Override
    public Cliente buscarPeloCpf(String cpf){
        Cliente cliente = dao.findByCpfCnpj(cpf);
        return cliente;
    }

    @Override
    public Cliente buscarPeloEmail(String email){
        Optional<Cliente> cliente = dao.findByEmail(email);
        return cliente.orElse(null);
    }
}
